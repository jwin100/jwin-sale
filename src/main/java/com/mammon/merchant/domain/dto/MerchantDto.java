package com.mammon.merchant.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dcl
 * @since 2024/1/16 17:38
 */
@Data
public class MerchantDto {

    /**
     * 商户名
     */
    @NotBlank(message = "商户名不能为空")
    private String merchantName;

    private String pcLogo;

    private String mobileLogo;
}
