package com.mammon.office.edition.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dcl
 * @date 2023-02-02 11:21:30
 */
@Data
public class IndustryAttrMapEntity {

    private String id;

    private String industryId;

    private String industryAttrId;

    private LocalDateTime createTime;
}
