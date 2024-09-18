package com.mammon.goods.gate;

import com.mammon.common.ResultJson;
import com.mammon.goods.service.SpuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-02-24 13:09:30
 */
@RestController
@RequestMapping("/gate/goods")
public class GateGoodsController {

    @Resource
    private SpuService spuService;

    @GetMapping("/barcode/{barcode}")
    public ResultJson findByBarcode(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @PathVariable("barcode") String barcode) {
        return ResultJson.ok(spuService.findDetailBySpuNo(merchantNo, barcode));
    }
}
