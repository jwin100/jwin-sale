package com.mammon.document.domain.vo;

import com.mammon.enums.CommonStatus;
import com.mammon.enums.IEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @since 2024/8/2 14:21
 */
@Data
public class DocumentHelpVo {

    private String id;

    private String resourceId;

    /**
     * 内容编码
     */
    private String code;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private int status;

    private String statusName;

    private LocalDateTime createTime;

    public String getStatusName() {
        return IEnum.getNameByCode(this.getStatus(), CommonStatus.class);
    }
}
