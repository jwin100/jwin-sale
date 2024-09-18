package com.mammon.feedback.domain.vo;

import com.mammon.enums.IEnum;
import com.mammon.feedback.domain.enums.FeedbackContactType;
import com.mammon.feedback.domain.enums.FeedbackStatus;
import com.mammon.feedback.domain.enums.FeedbackType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedbackVo {

    private String id;

    private long merchantNo;

    private long storeNo;

    private String storeName;

    private String accountId;

    /**
     * 反馈类型
     */
    private int type;

    private String typeName;

    private String title;

    private String content;

    private List<String> images;

    /**
     * 联系方式(微信，qq,电话)
     */
    private int contactType;

    private String contactTypeName;

    /**
     * 联系号码
     */
    private String contactNo;

    private int status;

    private String statusName;

    private String ip;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public String getTypeName() {
        return IEnum.getNameByCode(this.getType(), FeedbackType.class);
    }

    public String getContactTypeName() {
        return IEnum.getNameByCode(this.getContactType(), FeedbackContactType.class);
    }

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), FeedbackStatus.class);
    }
}
