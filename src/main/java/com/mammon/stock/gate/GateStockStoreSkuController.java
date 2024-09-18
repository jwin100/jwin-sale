package com.mammon.stock.gate;

import com.mammon.common.ResultJson;
import com.mammon.stock.domain.query.StockSkuCountedQuery;
import com.mammon.stock.domain.query.StockAllocateGateQuery;
import com.mammon.stock.domain.vo.StockSkuAllocateVo;
import com.mammon.stock.domain.vo.StockSkuCountedVo;
import com.mammon.stock.domain.vo.StockSkuVo;
import com.mammon.stock.service.StockSkuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/5/29 15:43
 */
@RestController
@RequestMapping("/gate/stock/sku")
public class GateStockStoreSkuController {

    @Resource
    private StockSkuService stockSkuService;

    @GetMapping("/{skuNo}")
    public ResultJson<StockSkuVo> list(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("skuNo") String skuNo) {
        return ResultJson.ok(stockSkuService.findBySkuNo(merchantNo, storeNo, skuNo));
    }

    @GetMapping("/list/pid/{spuId}")
    public ResultJson<List<StockSkuVo>> skuListBySpuId(@RequestHeader long merchantNo,
                                                       @RequestHeader long storeNo,
                                                       @RequestHeader String accountId,
                                                       @PathVariable String spuId) {
        return ResultJson.ok(stockSkuService.findListBySpuId(merchantNo, storeNo, spuId));
    }

    /**
     * 调拨库存信息获取
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param spuId
     * @param query
     * @return
     */
    @GetMapping("/allocate-list/{spuId}")
    public ResultJson<List<StockSkuAllocateVo>> skuAllocateListByPid(@RequestHeader long merchantNo,
                                                                     @RequestHeader long storeNo,
                                                                     @RequestHeader String accountId,
                                                                     @PathVariable("spuId") String spuId,
                                                                     @Validated StockAllocateGateQuery query) {
        return ResultJson.ok(stockSkuService.findAllocateBySpuId(merchantNo, storeNo, spuId, query));
    }

    /**
     * 计次商品sku列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/counted-list")
    public ResultJson<List<StockSkuCountedVo>> skuCountedList(@RequestHeader long merchantNo,
                                                              @RequestHeader long storeNo,
                                                              @RequestHeader String accountId,
                                                              StockSkuCountedQuery query) {
        return ResultJson.ok(stockSkuService.findCountedList(merchantNo, storeNo, query));
    }
}
