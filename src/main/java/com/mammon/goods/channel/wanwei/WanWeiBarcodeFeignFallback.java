package com.mammon.goods.channel.wanwei;

import cn.hutool.json.JSONUtil;
import com.mammon.common.ResultCode;
import com.mammon.common.ResultJson;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.goods.channel.wanwei.model.WanWeiBarCodeVo;
import com.mammon.utils.JsonUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @author dcl
 * @date 2023-03-01 10:25:58
 */
@Component
@Slf4j
public class WanWeiBarcodeFeignFallback implements FallbackFactory<WanWeiBarcodeFeign> {

    @Override
    public WanWeiBarcodeFeign create(Throwable cause) {
        ResultJson<?> result = new ResultJson<>(ResultCode.SERVICE_ERROR);
        if (cause instanceof CustomException) {
            result = ((CustomException) cause).getResultJson();
        }

        ResultJson<?> finalResult = result;
        return new WanWeiBarcodeFeign() {
            @Override
            public WanWeiBarCodeVo getBarCode(String authorization, String code) {
                if (finalResult.getData() != null) {
                    return JsonUtil.toObject((String) finalResult.getData(), WanWeiBarCodeVo.class);
                }
                WanWeiBarCodeVo vo = new WanWeiBarCodeVo();
                vo.setShowApiResCode(1);
                vo.setShowApiResError(finalResult.getMsg());
                return vo;
            }
        };
    }
}
