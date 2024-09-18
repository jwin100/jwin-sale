package com.mammon.print.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dcl
 * @since 2024/3/26 14:11
 */
@Data
public class PrintTerminalBindDto {

    private List<Integer> templateTypes = new ArrayList<>();
}
