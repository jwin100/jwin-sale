package com.mammon.clerk.domain.vo;

import com.mammon.clerk.domain.entity.ResourceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceVo extends ResourceEntity {

    private boolean hasChildren;
}
