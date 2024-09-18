package com.mammon.sms.channel.yunji;

import com.mammon.sms.channel.factory.SmsChannelFactory;
import com.mammon.sms.channel.factory.model.dto.BatchSmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsCallbackVo;
import com.mammon.sms.channel.factory.model.vo.SmsDetailVo;
import com.mammon.sms.channel.factory.model.dto.SmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsSendVo;
import com.mammon.sms.channel.yunji.enums.ResponseCodeConst;
import com.mammon.sms.channel.yunji.enums.SendItemStatusConst;
import com.mammon.sms.channel.yunji.model.YunJiConfigModel;
import com.mammon.sms.channel.yunji.model.dto.YunJiBatchSendDto;
import com.mammon.sms.channel.yunji.model.dto.YunJiSingleSendDto;
import com.mammon.sms.channel.yunji.model.vo.YunJiBatchSendVo;
import com.mammon.sms.channel.yunji.model.vo.YunJiSingleSendVo;
import com.mammon.sms.enums.SmsSendItemStatusEnum;
import com.mammon.sms.enums.SmsSendStatusEnum;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2022-10-13 11:03:03
 */
@Service("yunJi")
@Slf4j
public class YunJiService implements SmsChannelFactory {

    @Resource
    private YunJiFeign yunJiFeign;

    @Override
    public SmsSendVo smsSend(SmsSendDto dto) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());
        List<YunJiConfigModel> yunJiConfigs = JsonUtil.toList(dto.getConfigStr(), YunJiConfigModel.class);
        if (CollectionUtils.isEmpty(yunJiConfigs)) {
            log.error("YunJi发送失败,配置文件信息错误,{}", dto.getConfigStr());
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("内部发送信息异常");
            return vo;
        }

        YunJiConfigModel yunJiConfigModel = yunJiConfigs.stream()
                .filter(x -> x.getClassify() == dto.getSmsType()).findFirst()
                .orElse(null);
        if (yunJiConfigModel == null) {
            log.error("YunJi发送失败,配置文件信息错误,{}", dto.getSmsType());
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("内部发送信息异常");
            return vo;
        }
        return singleSend(dto, yunJiConfigModel);
    }

    @Override
    public SmsSendVo batchSmsSend(BatchSmsSendDto dto) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());
        List<YunJiConfigModel> yunJiConfigs = JsonUtil.toList(dto.getConfigStr(), YunJiConfigModel.class);
        if (CollectionUtils.isEmpty(yunJiConfigs)) {
            log.error("YunJi发送失败,配置文件信息错误,{}", dto.getConfigStr());
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("内部发送信息异常");
            return vo;
        }

        YunJiConfigModel yunJiConfigModel = yunJiConfigs.stream()
                .filter(x -> x.getClassify() == dto.getSmsType()).findFirst()
                .orElse(null);
        if (yunJiConfigModel == null) {
            log.error("YunJi发送失败,配置文件信息错误,{}", dto.getSmsType());
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("内部发送信息异常");
            return vo;
        }
        return batchSend(dto, yunJiConfigModel);
    }

    @Override
    public SmsCallbackVo smsCallback(Map<String, String> params) {
        return null;
    }

    @Override
    public Object callbackSuccess() {
        return null;
    }

    public SmsSendVo batchSend(BatchSmsSendDto dto, YunJiConfigModel model) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());

        YunJiBatchSendDto send = new YunJiBatchSendDto();
        send.setTaskId(dto.getSmsSendId());
        send.setAccessKey(model.getAccessKey());
        send.setAccessSecret(model.getAccessSecret());
        send.setClassificationSecret(model.getClassificationSecret());
        send.setPhones(dto.getPhone());
        send.setSignCode(dto.getSignCode());
        send.setTemplateCode(dto.getTempCode());
        send.setParams(dto.getParams());

        YunJiBatchSendVo sendVo = yunJiFeign.batchSend(send);
        log.info("yunJiBatchSend-result:{}", JsonUtil.toJSONString(sendVo));
        if (sendVo == null || sendVo.getBusinessException() == null) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("接口调用失败");
            return vo;
        }
        if (ResponseCodeConst.RESPONSE_CODE_SUCCESS != sendVo.getBusinessException().getResultCode()) {
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc(sendVo.getBusinessException().getResultMessage());
            return vo;
        }
        vo.setStatus(SmsSendStatusEnum.SEND_SUCCESS.getCode());
        List<SmsDetailVo> smsDetailVos = sendVo.getBusinessData().getList().stream().map(x -> {
            SmsDetailVo smsDetailVo = new SmsDetailVo();
            smsDetailVo.setPhone(x.getPhone());
            smsDetailVo.setStatus(SmsSendItemStatusEnum.SUCCESS.getCode());
            if (SendItemStatusConst.SEND_ITEM_STATUS_SUCCESS != x.getCode()) {
                smsDetailVo.setStatus(SmsSendItemStatusEnum.FAIL.getCode());
                smsDetailVo.setErrorDesc(x.getMsg());
            }
            return smsDetailVo;
        }).collect(Collectors.toList());
        vo.setResDetail(smsDetailVos);
        return vo;
    }

    public SmsSendVo singleSend(SmsSendDto dto, YunJiConfigModel model) {
        SmsSendVo vo = new SmsSendVo();
        vo.setSmsSendId(dto.getSmsSendId());

        YunJiSingleSendDto send = new YunJiSingleSendDto();
        send.setAccessKey(model.getAccessKey());
        send.setAccessSecret(model.getAccessSecret());
        send.setClassificationSecret(model.getClassificationSecret());
        send.setPhone(dto.getPhone().get(0));
        send.setSignCode(dto.getSignCode());
        send.setTemplateCode(dto.getTempCode());
        send.setParams(dto.getParams());

        try {
            YunJiSingleSendVo sendVo = yunJiFeign.singleSend(send);
            log.info("yunJiSingleSend-result:{}", JsonUtil.toJSONString(sendVo));
            if (sendVo == null || sendVo.getBusinessException() == null) {
                vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
                vo.setErrorDesc("接口调用失败");
                return vo;
            }
            if (ResponseCodeConst.RESPONSE_CODE_SUCCESS != sendVo.getBusinessException().getResultCode()) {
                vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
                vo.setErrorDesc(sendVo.getBusinessException().getResultMessage());
                return vo;
            }
            vo.setStatus(SmsSendStatusEnum.SEND_SUCCESS.getCode());
            SmsDetailVo smsDetailVo = new SmsDetailVo();
            smsDetailVo.setPhone(sendVo.getBusinessData().getPhone());
            smsDetailVo.setStatus(SmsSendItemStatusEnum.SUCCESS.getCode());
            if (SendItemStatusConst.SEND_ITEM_STATUS_SUCCESS != sendVo.getBusinessData().getCode()) {
                smsDetailVo.setStatus(SmsSendItemStatusEnum.FAIL.getCode());
                smsDetailVo.setErrorDesc(sendVo.getBusinessData().getMsg());
            }
            List<SmsDetailVo> smsDetailVos = new ArrayList<>();
            smsDetailVos.add(smsDetailVo);
            vo.setResDetail(smsDetailVos);
            return vo;
        } catch (Exception e) {
            log.error("第三方发送错误,:{}", e.getMessage(), e);
            vo.setStatus(SmsSendStatusEnum.SEND_FAIL.getCode());
            vo.setErrorDesc("第三方发送错误");
            return vo;
        }
    }
}
