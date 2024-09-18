package com.mammon.sms.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mammon.common.Generate;
import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.config.ApplicationBean;
import com.mammon.exception.CustomException;
import com.mammon.merchant.domain.vo.MerchantStoreVo;
import com.mammon.merchant.service.MerchantStoreService;
import com.mammon.office.order.common.ConvertServletParam;
import com.mammon.sms.channel.factory.SmsChannelFactory;
import com.mammon.sms.channel.factory.model.dto.BatchSmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsCallbackVo;
import com.mammon.sms.channel.factory.model.dto.SmsSendDto;
import com.mammon.sms.channel.factory.model.vo.SmsSendVo;
import com.mammon.sms.dao.SmsSendDao;
import com.mammon.sms.domain.dto.*;
import com.mammon.sms.domain.entity.*;
import com.mammon.sms.domain.vo.SmsSendPageVo;
import com.mammon.sms.enums.*;
import com.mammon.sms.utils.StrReplaceUtil;
import com.mammon.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SmsSendService {

    private static final int SMS_MESSAGE_INNER_MAX_LENGTH = 50;
    private static final int SMS_MESSAGE_CHANNEL_MAX_LENGTH = 60;

    @Resource
    private SmsSendDao smsSendDao;

    @Resource
    private SmsSignService smsSignService;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Resource
    private SmsSendItemService smsSendItemService;

    @Resource
    private SmsChannelService smsChannelService;

    @Resource
    private SmsService smsService;

    @Resource
    private MerchantStoreService merchantStoreService;

    @Resource
    private SmsSignChannelRelService smsSignChannelRelService;

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    /**
     * 异步发送，不返回系统异常(系统自动触发，走单条发送)
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void asyncSend(SmsSendNoticeDto dto) {
        validSmsEnable(dto.getMerchantNo());

        // 保存发送记录
        SmsSendEntity entity = new SmsSendEntity();
        entity.setMerchantNo(dto.getMerchantNo());
        entity.setStoreNo(dto.getStoreNo());
        entity.setReturnCnt(0);
        entity.setSendAccountId(dto.getAccountId());
        entity.setFree(makeFree(dto.getMerchantNo()));
        entity.setTempType(dto.getTempType());
        entity.setSendTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        setSmsRecord(entity, dto.getUsers(), dto.getTempParams());
        smsSendDao.save(entity);
        smsSendItemService.batchSave(entity.getId(), dto.getUsers(), entity.getSendMessage());
        // 预扣费
        smsRecharge(entity);
        if (entity.getStatus() == SmsSendStatusEnum.SENDING.getCode()) {
            log.info("短信已体提交：{}", JsonUtil.toJSONString(entity));
            smsChannelSend(entity);
        }
    }

    private void setSmsRecord(SmsSendEntity entity, List<SmsSendUserDto> users, Map<String, String> tempParams) {
        if (CollUtil.isEmpty(users)) {
            throw new CustomException("发送人不能为空");
        }
        SmsSignEntity sign = validSmsSign(entity.getMerchantNo());
        SmsTemplateEntity template = validSmsTemplate(entity.getMerchantNo(), entity.getTempType());

        String sendId = Generate.generateUUID();
        int tempGroup = template.getTempGroup();
        int smsType = template.getSmsType();
        String content = makeContent(sign.getSignName(), smsType, template.getTemplate(), tempParams);
        String params = JsonUtil.toJSONString(tempParams);
        int messageLength = content.length();
        int messageCnt = (int) Math.ceil((double) messageLength / SMS_MESSAGE_INNER_MAX_LENGTH);
        int messageChannelCnt = (int) Math.ceil((double) messageLength / SMS_MESSAGE_CHANNEL_MAX_LENGTH);
        int consumeCnt = messageCnt * users.size();
        int status = smsType == SmsTypeEnum.MARKET.getCode() ?
                SmsSendStatusEnum.EXAMINE_WAIT.getCode() :
                SmsSendStatusEnum.SENDING.getCode();
        SmsChannelEntity smsChannel = getSmsChannel(smsType);

        entity.setId(sendId);
        entity.setSignId(sign.getId());
        entity.setTempId(template.getId());
        entity.setReturnCnt(0);
        entity.setTempGroup(tempGroup);
        entity.setSmsType(smsType);
        entity.setSendMessage(content);
        entity.setMessageParams(params);
        entity.setMessageLength(messageLength);
        entity.setMessageCnt(messageCnt);
        entity.setMessageChannelCnt(messageChannelCnt);
        entity.setConsumeCnt(consumeCnt);
        entity.setSmsChannelId(smsChannel.getId());
        entity.setStatus(status);
    }

    /**
     * 同步发送，同步返回系统异常(页面手动发送)
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncSend(long merchantNo, long storeNo, String accountId, SmsSendRecordDto dto) {
        validSmsEnable(merchantNo);
        validSmsSign(merchantNo, dto.getSignId());

        String sendId = Generate.generateUUID();
        int tempType = SmsTempTypeEnum.OTHER.getCode();
        int smsType = SmsTypeEnum.MARKET.getCode();
        int tempGroup = merchantNo == 0 ? SmsTempGroupEnum.SYSTEM_SMS.getCode() : SmsTempGroupEnum.MERCHANT_SMS.getCode();
        int messageLength = dto.getSendMessage().length();
        int messageCnt = (int) Math.ceil((double) messageLength / SMS_MESSAGE_INNER_MAX_LENGTH);
        int messageChannelCnt = (int) Math.ceil((double) messageLength / SMS_MESSAGE_CHANNEL_MAX_LENGTH);
        int consumeCnt = messageCnt * dto.getUsers().size();
        SmsChannelEntity smsChannel = getSmsChannel(smsType);

        SmsSendEntity entity = new SmsSendEntity();
        entity.setId(sendId);
        entity.setMerchantNo(merchantNo);
        entity.setStoreNo(storeNo);
        entity.setSignId(dto.getSignId());
        entity.setReturnCnt(0);
        entity.setSendAccountId(accountId);
        entity.setFree(makeFree(merchantNo));
        entity.setTempGroup(tempGroup);
        entity.setTempType(tempType);
        entity.setSmsType(smsType);
        entity.setSendMessage(dto.getSendMessage());
        entity.setMessageLength(messageLength);
        entity.setMessageCnt(messageCnt);
        entity.setMessageChannelCnt(messageChannelCnt);
        entity.setConsumeCnt(consumeCnt);
        entity.setSmsChannelId(smsChannel.getId());
        entity.setStatus(SmsSendStatusEnum.EXAMINE_WAIT.getCode());
        entity.setSendTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        smsSendDao.save(entity);
        smsSendItemService.batchSave(sendId, dto.getUsers(), dto.getSendMessage());
        //预扣费
        smsRecharge(entity);
        if (entity.getStatus() == SmsSendStatusEnum.SENDING.getCode()) {
            smsChannelSend(entity);
        }
    }

    /**
     * 页面手动触发的都走批量发送
     * 批量发送延后知道发送结果
     *
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSend(SmsSendEntity entity) {
        List<SmsSendItemEntity> items = smsSendItemService.findAllBySmsSendId(entity.getId());
        int batchSize = 200;
        int totalCnt = items.size();
        int batchCnt = totalCnt % batchSize == 0 ? totalCnt / batchSize : totalCnt / batchSize + 1;
        for (int i = 1; i <= batchCnt; i++) {
            int skip = (i - 1) * batchSize;
            List<SmsSendItemEntity> sendItems = items.stream().skip(skip).limit(batchSize).collect(Collectors.toList());
            //直接调用发送接口,塞到第三方通道
            smsChannelBatchSend(entity, sendItems);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void smsChannelSend(SmsSendEntity entity) {
        SmsChannelEntity smsChannel = smsChannelService.findById(entity.getSmsChannelId());
        if (smsChannel == null) {
            throw new CustomException("发送渠道错误");
        }
        List<SmsSendItemEntity> items = smsSendItemService.findAllBySmsSendId(entity.getId());
        Map<String, Object> params = null;
        if (StringUtils.isNotBlank(entity.getMessageParams())) {
            JSONObject jsonObject = JSONUtil.parseObj(entity.getMessageParams());
            params = BeanUtil.beanToMap(jsonObject, false, true);
        }

        SmsChannelFactory factory = ApplicationBean.getBean(smsChannel.getChannelCode(), SmsChannelFactory.class);
        SmsSendDto dto = new SmsSendDto();
        dto.setSmsSendId(entity.getId());
        dto.setSmsType(entity.getSmsType());
        dto.setConfigStr(smsChannel.getConfigStr());
        dto.setPhone(items.stream().map(SmsSendItemEntity::getPhone).collect(Collectors.toList()));
        dto.setContent(entity.getSendMessage());
        dto.setSendTime(entity.getSendTime());
        dto.setParams(params);
        SmsSendVo smsSendVo = factory.smsSend(dto);
        //幂等修改，防止重复修改
        updateStatus(entity.getId(), entity.getStatus(), smsSendVo.getStatus(), smsSendVo.getErrorDesc());
        smsSendVo.getResDetail().forEach(x -> {
            smsSendItemService.updateStatus(entity.getId(), x.getPhone(), SmsSendItemStatusEnum.WAITING.getCode(),
                    x.getStatus(), x.getErrorDesc());
        });

        // 未扣费短信不做短信额度返还操作
        if (entity.getFree() == 1) {
            return;
        }

        if (smsSendVo.getStatus() == SmsSendStatusEnum.SEND_FAIL.getCode()) {
            //全部失败，整体回滚
            smsService.smsRechargeChange(entity.getMerchantNo(), entity.getStoreNo(), SmsRechargeLogTypeConst.返还,
                    entity.getId(), null, entity.getConsumeCnt(), "发送失败返还");
        } else {
            //检查部分,部分失败回滚
            long errCount = smsSendVo.getResDetail().stream()
                    .filter(x -> x.getStatus() == SmsSendItemStatusEnum.FAIL.getCode()).count();
            if (errCount > 0) {
                int cnt = entity.getMessageCnt() * (int) errCount;
                smsService.smsRechargeChange(entity.getMerchantNo(), entity.getStoreNo(), SmsRechargeLogTypeConst.返还,
                        entity.getId(), null, cnt, "发送失败返还");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void smsChannelBatchSend(SmsSendEntity entity, List<SmsSendItemEntity> items) {
        SmsChannelEntity smsChannel = smsChannelService.findById(entity.getSmsChannelId());
        if (smsChannel == null) {
            throw new CustomException("发送渠道错误");
        }
        Map<String, Object> params = null;
        if (StringUtils.isNotBlank(entity.getMessageParams())) {
            JSONObject jsonObject = JSONUtil.parseObj(entity.getMessageParams());
            params = BeanUtil.beanToMap(jsonObject, false, true);
        }

        SmsChannelFactory factory = ApplicationBean.getBean(smsChannel.getChannelCode(), SmsChannelFactory.class);
        BatchSmsSendDto dto = new BatchSmsSendDto();
        dto.setSmsSendId(entity.getId());
        dto.setSmsType(entity.getSmsType());
        dto.setConfigStr(smsChannel.getConfigStr());
        dto.setPhone(items.stream().map(SmsSendItemEntity::getPhone).collect(Collectors.toList()));
        dto.setContent(entity.getSendMessage());
        dto.setSendTime(entity.getSendTime());
        dto.setParams(params);
        SmsSendVo smsSendVo = factory.batchSmsSend(dto);
        //幂等修改，防止重复修改
        updateStatus(entity.getId(), SmsSendStatusEnum.SENDING.getCode(), SmsSendStatusEnum.SEND_SUCCESS.getCode(),
                smsSendVo.getErrorDesc());
        smsSendVo.getResDetail().forEach(x -> {
            smsSendItemService.updateStatus(entity.getId(), x.getPhone(), SmsSendItemStatusEnum.WAITING.getCode(),
                    x.getStatus(), x.getErrorDesc());
        });

        if (smsSendVo.getStatus() == SmsSendStatusEnum.SEND_FAIL.getCode() && entity.getFree() == 0) {
            //全部失败，整体回滚
            smsService.smsRechargeChange(entity.getMerchantNo(), entity.getStoreNo(), SmsRechargeLogTypeConst.返还,
                    entity.getId(), null, entity.getConsumeCnt(), "发送失败返还");
        }
    }

    /**
     * 预扣费
     */
    public void smsRecharge(SmsSendEntity entity) {
        if (entity.getFree() == 0) {
            SmsEntity sms = smsService.smsInfo(entity.getMerchantNo());
            if (sms != null && sms.getRecharge() < entity.getConsumeCnt()) {
                updateStatus(entity.getId(), entity.getStatus(), SmsSendItemStatusEnum.FAIL.getCode(), "短信余额不足");
                return;
            }
            // 预扣费
            smsService.smsRechargeChange(entity.getMerchantNo(), entity.getStoreNo(), SmsRechargeLogTypeConst.消费,
                    entity.getId(), null, -entity.getConsumeCnt(), "发送短信扣除");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void smsExamine(List<String> ids, SmsSendExamineDto dto) {
        if (dto.getStatus() != SmsSendStatusEnum.SENDING.getCode() &&
                dto.getStatus() != SmsSendStatusEnum.EXAMINE_FAIL.getCode()) {
            return;
        }
        List<SmsSendEntity> list = smsSendDao.findAllByIds(ids);
        list.forEach(x -> {
            int result = updateStatus(x.getId(), SmsSendStatusEnum.EXAMINE_WAIT.getCode(), dto.getStatus(), dto.getRemark());
            if (result > 0 && dto.getStatus() != SmsSendStatusEnum.SENDING.getCode()) {
                batchSend(x);
            }
        });
    }

    private int updateStatus(String id, int beforeStatus, int afterStatus, String errorDesc) {
        return smsSendDao.updateStatus(id, beforeStatus, afterStatus, errorDesc);
    }

    public Object smsCallback(String channelCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> paramMap = ConvertServletParam.convertParam(request);
        SmsChannelFactory factory = ApplicationBean.getBean(channelCode, SmsChannelFactory.class);
        if (factory == null) {
            return "0";
        }
        SmsCallbackVo callbackVo = factory.smsCallback(paramMap);
        smsSendItemService.updateStatus(callbackVo.getSmsId(), callbackVo.getPhone(), SmsSendItemStatusEnum.WAITING.getCode(),
                callbackVo.getStatus(), callbackVo.getErrMsg());
        SmsSendEntity sms = smsSendDao.findById(callbackVo.getSmsId());
        List<SmsSendItemEntity> items = smsSendItemService.findAllBySmsSendId(callbackVo.getSmsId());
        if (sms == null) {
            return "0";
        }
        // lock
        if (sms.getFree() == 0 && sms.getReturnCnt() == 0) {
            long waitTotal = items.stream().filter(x -> x.getStatus() == SmsSendItemStatusEnum.WAITING.getCode()).count();
            if (waitTotal == 0) {
                //检查部分,部分失败回滚
                long errCount = items.stream()
                        .filter(x -> x.getStatus() == SmsSendItemStatusEnum.FAIL.getCode()).count();
                if (errCount > 0) {
                    int cnt = sms.getMessageCnt() * (int) errCount;
                    smsService.smsRechargeChange(sms.getMerchantNo(), sms.getStoreNo(), SmsRechargeLogTypeConst.返还,
                            sms.getId(), null, cnt, "发送失败返还");
                }
            }
        }
        return factory.callbackSuccess();
    }

    public PageVo<SmsSendPageVo> page(long merchantNo, long storeNo, String accountId, SmsQuery dto) {
        int total = smsSendDao.countPage(merchantNo, storeNo, accountId, dto);
        if (total <= 0) {
            return null;
        }
        List<SmsSendEntity> list = smsSendDao.findPage(merchantNo, storeNo, accountId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.of();
        }
        List<Long> storeNos = list.stream().map(SmsSendEntity::getStoreNo).distinct().collect(Collectors.toList());
        List<MerchantStoreVo> stores = merchantStoreService.findAllByStoreNos(merchantNo, storeNos);
        List<SmsSendPageVo> vos = list.stream().map(x -> {
            SmsSendPageVo vo = new SmsSendPageVo();
            BeanUtils.copyProperties(x, vo);
            MerchantStoreVo store = stores.stream().filter(y -> y.getStoreNo() == x.getStoreNo()).findFirst().orElse(null);
            if (store != null) {
                vo.setStoreName(store.getStoreName());
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResult.of(dto.getPageIndex(), dto.getPageSize(), total, vos);
    }

    private SmsEntity validSmsEnable(long merchantNo) {
        SmsEntity sms = smsService.smsInfo(merchantNo);
        if (sms == null || sms.getStatus() != 1) {
            throw new CustomException("短信未开通");
        }
        return sms;
    }

    private SmsSignEntity validSmsSign(long merchantNo) {
        SmsSignEntity sign = smsSignService.findByDefaultStatus(merchantNo);
        if (sign == null || sign.getStatus() != SmsSignStatusEnum.SUCCESS.getCode()) {
            throw new CustomException("短信签名错误");
        }
        return sign;
    }

    private SmsSignEntity validSmsSign(long merchantNo, String signId) {
        SmsSignEntity sign = smsSignService.findById(merchantNo, signId);
        if (sign == null || sign.getStatus() != SmsSignStatusEnum.SUCCESS.getCode()) {
            throw new CustomException("短信签名错误");
        }
        return sign;
    }

    private SmsTemplateEntity validSmsTemplate(long merchantNo, int tempType) {
        SmsTemplateEntity template = smsTemplateSettingService.findByTempType(merchantNo, tempType);
        if (template == null || template.getStatus() != SmsSignStatusEnum.SUCCESS.getCode()) {
            log.error("短信模板错误:{}", template);
            throw new CustomException("短信模板错误");
        }
        return template;
    }

    private SmsChannelEntity getSmsChannel(int smsType) {
        SmsChannelEntity smsChannel = smsChannelService.findBySmsType(smsType);
        if (smsChannel == null) {
            throw new CustomException("发送渠道错误");
        }
        return smsChannel;
    }

    private int makeFree(long merchantNo) {
        return merchantNo == 0 ? 1 : 0;
    }

    private String makeContent(String singName, int smsType, String tempMessage, Map<String, String> tempParams) {
        String content = tempMessage;
        if (smsType == SmsTypeEnum.MARKET.getCode()) {
            String contentSuffix = "回复T退订";
            if (!content.contains(contentSuffix)) {
                content = String.format("%s，%s", content, contentSuffix);
            }
        }

        int strStartIndex = content.indexOf("【");
        int strEndIndex = content.indexOf("】");
        if (strStartIndex >= 0 && strEndIndex > 0) {
            content = content.substring(strEndIndex);
        }
        content = String.format("【%s】%s", singName, content);
        if (tempParams != null) {
            content = StrReplaceUtil.tempReplace(tempParams, content);
        }
        return content;
    }
}
