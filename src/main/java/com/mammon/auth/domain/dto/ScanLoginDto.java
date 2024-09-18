package com.mammon.auth.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dcl
 * @since 2024/5/23 15:41
 */
@Data
public class ScanLoginDto {
    /**
     * 扫码id
     */
    @NotBlank(message = "扫码id不能为空")
    private String scanId;
}
