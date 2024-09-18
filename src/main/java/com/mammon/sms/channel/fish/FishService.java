package com.mammon.sms.channel.fish;

import cn.hutool.json.JSONUtil;
import com.mammon.config.JsonMapper;
import com.mammon.sms.channel.factory.SmsChannelFactory;
import com.mammon.sms.channel.factory.model.dto.BatchSmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsCallbackVo;
import com.mammon.sms.channel.factory.model.vo.SmsDetailVo;
import com.mammon.sms.channel.factory.model.dto.SmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsSendVo;
import com.mammon.sms.channel.fish.enums.FishStateConst;
import com.mammon.sms.channel.fish.model.*;
import com.mammon.sms.channel.fish.model.dto.FishBatchSendDto;
import com.mammon.sms.channel.fish.model.dto.FishBatchSendItemDto;
import com.mammon.sms.channel.fish.model.dto.FishSendDto;
import com.mammon.sms.channel.fish.model.vo.FishSendResultVo;
import com.mammon.sms.enums.SmsSendItemStatusEnum;
import com.mammon.sms.enums.SmsSendStatusEnum;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("fish")
@Slf4j
public class FishService implements SmsChannelFactory {

    @Resource
    private FishClient fishClient;

    @Override
    public SmsSendVo smsSend(SmsSendDto dto) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());
        FishConfigModel fishConfig = JsonUtil.toObject(dto.getConfigStr(), FishConfigModel.class);
        if (fishConfig == null) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("发送渠道信息异常");
            return vo;
        }
        String content = urlEncoder(dto.getContent());
        if (StringUtils.isBlank(content)) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("编码异常");
            return vo;
        }

        FishSendDto send = new FishSendDto();
        send.setAccount(fishConfig.getAccount());
        send.setPassword(DigestUtils.md5Hex(fishConfig.getPassword()));
        send.setSmsType(fishConfig.getSmsType());
        send.setMobile(String.join(",", dto.getPhone()));
        send.setContent(content);
        send.setSubCode(String.valueOf(fishConfig.getSubCode()));
        send.setVersion(fishConfig.getVersion());
        send.setSmsId(dto.getSmsSendId());
        if (dto.getSendTime() != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(JsonMapper.DATE_TIME_FORMATTER);
            send.setSendTime(df.format(dto.getSendTime()));
        }

        FishSendResultVo result = fishClient.send(send);
        log.info("fish-result:{}", JsonUtil.toJSONString(result));
        if (result == null) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("发送渠道调用异常");
            return vo;
        }
        if (!FishStateConst.SUCCESS_CODE.equals(result.getSubStat())) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc(result.getSubStatDes());
            return vo;
        }

        vo.setStatus(SmsSendStatusEnum.SEND_SUCCESS.getCode());
        List<SmsDetailVo> smsDetailVos = result.getResDetail().stream().map(x -> {
            SmsDetailVo smsDetailVo = new SmsDetailVo();
            smsDetailVo.setPhone(x.getPhoneNumber());
            smsDetailVo.setStatus(SmsSendItemStatusEnum.SUCCESS.getCode());
            if (!FishStateConst.SUCCESS_CODE.equals(x.getStat())) {
                smsDetailVo.setStatus(SmsSendItemStatusEnum.FAIL.getCode());
                smsDetailVo.setErrorDesc(x.getStatDes());
            }
            return smsDetailVo;
        }).collect(Collectors.toList());
        vo.setResDetail(smsDetailVos);
        return vo;
    }

    @Override
    public SmsSendVo batchSmsSend(BatchSmsSendDto dto) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());
        FishConfigModel fishConfig = JsonUtil.toObject(dto.getConfigStr(), FishConfigModel.class);
        if (fishConfig == null) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("发送渠道信息异常");
            return vo;
        }

        String content = urlEncoder(dto.getContent());
        if (StringUtils.isBlank(content)) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("编码异常");
            return vo;
        }

        List<FishBatchSendItemDto> sendItems = dto.getPhone().stream().map(x -> {
            FishBatchSendItemDto itemDto = new FishBatchSendItemDto();
            itemDto.setMobile(x);
            itemDto.setContent(content);
            return itemDto;
        }).collect(Collectors.toList());

        FishBatchSendDto send = new FishBatchSendDto();
        send.setAccount(fishConfig.getAccount());
        send.setPassword(DigestUtils.md5Hex(fishConfig.getPassword()));
        send.setMobiles(sendItems);
        send.setSubCode(String.valueOf(fishConfig.getSubCode()));
        send.setSmsId(dto.getSmsSendId());
        if (dto.getSendTime() != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(JsonMapper.DATE_TIME_FORMATTER);
            send.setSendTime(df.format(dto.getSendTime()));
        }

        FishSendResultVo result = fishClient.batchSend(send);
        if (result == null) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("接口调用失败");
            return vo;
        }
        if (!FishStateConst.SUCCESS_CODE.equals(result.getSubStat())) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc(result.getSubStatDes());
            return vo;
        }
        vo.setStatus(SmsSendStatusEnum.SEND_SUCCESS.getCode());
        return vo;
    }

    @Override
    public SmsCallbackVo smsCallback(Map<String, String> params) {
        SmsCallbackVo vo = new SmsCallbackVo();
        if (params.containsKey("phoneNumber")) {
            vo.setPhone(params.get("phoneNumber"));
        }
        if (params.containsKey("smsId")) {
            vo.setSmsId(params.get("smsId"));
        }
        if (params.containsKey("stat")) {
            if (Integer.parseInt(params.get("stat")) == 0) {
                vo.setStatus(SmsSendItemStatusEnum.SUCCESS.getCode());
            } else {
                vo.setStatus(SmsSendItemStatusEnum.SUCCESS.getCode());
            }
        }
        if (params.containsKey("statDes")) {
            vo.setErrCode(params.get("statDes"));
            vo.setErrMsg(getErrMsg(vo.getErrCode()));
        }
        if (params.containsKey("revTime")) {
            String revTime = params.get("revTime");
            if (StringUtils.isNotBlank(revTime)) {
                long timestamp = Long.parseLong(revTime);
                vo.setSendTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
            }
        }
        return vo;
    }

    @Override
    public Object callbackSuccess() {
        return "0";
    }

    private String urlEncoder(String content) {
        try {
            return URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String getErrMsg(String errCode) {
        switch (errCode) {
            case "r:000":
                return "处理成功";
            case "r:001":
                return "内容错误";
            case "r:002":
                return "号码错误";
            case "r:003":
                // 账号或密码错误
            case "r:004":
                // 余额不足
                return "渠道信息错误";
            case "r:006":
                return "提交号码数量过多";
            case "r:007":
                return "业务类型错误";
            case "r:008":
                return "没有权限";
            case "r:010":
                return "访问速度过快";
            case "r:011":
                return "解密失败";
            case "r:999":
                return "其他错误";
            default:
                return errCode;
        }
    }
}
