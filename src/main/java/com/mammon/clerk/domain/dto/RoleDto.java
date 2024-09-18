package com.mammon.clerk.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleDto {

    private String name;

    private String remark;
    
    private int type;

    private List<PowerCreateDto> powers = new ArrayList<>();
}
