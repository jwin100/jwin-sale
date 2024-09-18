package com.mammon.stock.gate;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.enums.CommonStatus;
import com.mammon.stock.domain.query.StockSpuPageQuery;
import com.mammon.stock.domain.query.StockSpuQuery;
import com.mammon.stock.domain.vo.StockSpuListVo;
import com.mammon.stock.service.StockSpuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/5/29 15:43
 */
@RestController
@RequestMapping("/gate/stock/spu")
public class GateStockStoreSpuController {

    @Resource
    private StockSpuService stockSpuService;

    @GetMapping("/list")
    public ResultJson<List<StockSpuListVo>> list(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 StockSpuQuery query) {
        return ResultJson.ok(stockSpuService.findList(merchantNo, storeNo, query));
    }

    /**
     * 收银台商品分页搜索
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<StockSpuListVo>> page(@RequestHeader long merchantNo,
                                                   @RequestHeader long storeNo,
                                                   @RequestHeader String accountId,
                                                   @Validated StockSpuPageQuery query) {
        query.setStatus(CommonStatus.ENABLED.getCode());
        return ResultJson.ok(stockSpuService.page(merchantNo, storeNo, accountId, query));
    }
}
