package com.mammon.goods.channel.wanwei;

import com.mammon.goods.channel.wanwei.model.WanWeiBarCodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author dcl
 * @since 2024/4/11 18:24
 */
@FeignClient(name = "barcode", url = "https://ali-barcode.showapi.com",
        fallbackFactory = WanWeiBarcodeFeignFallback.class)
public interface WanWeiBarcodeFeign {

    @GetMapping("/barcode")
    WanWeiBarCodeVo getBarCode(@RequestHeader("Authorization") String authorization, @RequestParam("code") String code);
}
