package com.mammon.print.channel.elind.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElindOauthDto extends ElindBasicPrintDto {

    @JsonProperty("grant_type")
    private String grantType = "client_credentials";

    private String scope = "all";
}