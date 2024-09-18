package com.mammon.goods.channel.wanwei;

import cn.hutool.json.JSONUtil;
import com.mammon.biz.service.FileService;
import com.mammon.common.ResultCode;
import com.mammon.config.JsonMapper;
import com.mammon.exception.CustomException;
import com.mammon.goods.channel.factory.BarcodeChannel;
import com.mammon.goods.channel.wanwei.model.WanWeiBarCodeVo;
import com.mammon.goods.channel.wanwei.model.WanWeiBarcodeDetailVo;
import com.mammon.goods.domain.entity.GoodsEntity;
import com.mammon.goods.domain.model.GoodsImgModel;
import com.mammon.utils.AmountUtil;
import com.mammon.utils.JsonUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/11 18:24
 */
@Service
@Slf4j
public class WanWeiBarcodeService implements BarcodeChannel {

    private static final String AUTHORIZATION_BASE = "APPCODE ";
    private static final String APP_CODE = "760b4c6faecc465b93f60db7c8bea907";

    @Resource
    private WanWeiBarcodeFeign wanWeiBarcodeFeign;

    @Resource
    private FileService fileService;

    @Override
    public GoodsEntity getBarcode(String code) {
        String authorization = AUTHORIZATION_BASE + APP_CODE;
        WanWeiBarCodeVo barCodeVo = wanWeiBarcodeFeign.getBarCode(authorization, code);
        log.info("barcodeResult:{}", barCodeVo);
        if (barCodeVo == null) {
            log.info("remote barcode is null :{}", code);
            throw new CustomException(ResultCode.SERVICE_ERROR);
        }
        if (barCodeVo.getShowApiResCode() != 0) {
            log.info("remote barcode is {} :{}", barCodeVo.getShowApiResError(), code);
            throw new CustomException(barCodeVo.getShowApiResError());
        }
        WanWeiBarcodeDetailVo vo = barCodeVo.getShowApiResBody();
        if (vo == null) {
            log.info("remote barcode is null :{}", code);
            return null;
        }
        if (!"0".equals(vo.getRetCode())) {
            log.info("remote barcode is null :{}", code);
            throw new CustomException(vo.getRemark());
        }
        GoodsEntity entity = new GoodsEntity();
        entity.setBarcode(vo.getCode());
        entity.setName(vo.getGoodsName());
        entity.setClassCode(vo.getGoodsType());
        entity.setSpecification(vo.getSpec());
        entity.setPrice(AmountUtil.parse(vo.getPrice()));
        entity.setCodeSource(vo.getYcg());
        entity.setFirmName(vo.getManuName());
        if (StringUtils.isNotBlank(vo.getImg())) {
            List<String> imgs = Collections.singletonList(vo.getImg());
            List<String> images = imgs.stream()
                    .map(x -> fileService.uploadFile(0, x))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            entity.setImage(JsonUtil.toJSONString(images));
        }
        return entity;
    }
}
