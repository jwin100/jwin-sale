package com.mammon.sms.domain.vo;

import com.mammon.sms.enums.SmsTypeEnum;
import com.mammon.sms.enums.SmsTempTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsSendPageVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

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

    private long messageLength;

    /**
     * 内容计数
     */
    private int messageCnt;

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
     * 提交日期
     */
    private LocalDateTime createTime;
}
