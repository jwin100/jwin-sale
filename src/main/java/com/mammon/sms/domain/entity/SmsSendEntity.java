package com.mammon.sms.domain.entity;

import com.mammon.sms.enums.SmsTypeEnum;
import com.mammon.sms.enums.SmsTempTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 发送记录
 */
@Data
public class SmsSendEntity {

    private String id;

    private long merchantNo;

    private long storeNo;

    /**
     * 签名id
     */
    private String signId;

    /**
     * 模板id
     */
    private String tempId;

    /**
     * 模板组
     *
     * @see com.mammon.sms.enums.SmsTempGroupEnum
     */
    private int tempGroup;

    /**
     * 短信类型
     *
     * @see SmsTypeEnum
     */
    private int smsType;

    /**
     * 模板类型
     *
     * @see SmsTempTypeEnum
     */
    private int tempType;

    /**
     * 发送内容
     */
    private String sendMessage;

    private String messageParams;

    /**
     * 发送内容长度
     */
    private long messageLength;

    /**
     * 内容计数
     */
    private int messageCnt;

    /**
     * 渠道方内容计数(计算使用短信条数)
     */
    private int messageChannelCnt;

    /**
     * 是否免费(1:是,0:否)
     */
    private int free;

    /**
     * 计费预扣(messageCnt*手机号)
     */
    private int consumeCnt;

    /**
     * 失败返还
     */
    private int returnCnt;


    /**
     * 状态
     *
     * @see com.mammon.sms.enums.SmsSendStatusEnum
     */
    private int status;

    /**
     * 失败表述
     */
    private String errorDesc;

    /**
     * 发送日期
     */
    private LocalDateTime sendTime;

    /**
     * 发送渠道
     */
    private String smsChannelId;

    /**
     * 发送人
     */
    private String sendAccountId;

    /**
     * 提交日期
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
