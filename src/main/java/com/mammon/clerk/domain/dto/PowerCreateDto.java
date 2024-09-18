package com.mammon.clerk.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PowerCreateDto {

    private String id;

    private int index;

    private List<PowerCreateDto> children = new ArrayList<>();
}
