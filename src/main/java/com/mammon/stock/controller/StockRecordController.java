package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.vo.StockRecordVo;
import com.mammon.stock.domain.dto.StockRecordDto;
import com.mammon.stock.domain.query.StockRecordQuery;
import com.mammon.stock.service.StockRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 出入库记录
 */
@RestController
@RequestMapping("/stock/record")
public class StockRecordController {

    @Resource
    private StockRecordService stockRecordService;

    /**
     * 新增出出入库记录
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody StockRecordDto dto) {
        stockRecordService.save(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 出入库记录详情
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultJson<StockRecordVo> info(@RequestHeader long merchantNo,
                                          @RequestHeader long storeNo,
                                          @RequestHeader String accountId,
                                          @PathVariable("id") String id) {
        return ResultJson.ok(stockRecordService.info(merchantNo, storeNo, accountId, id));
    }

    /**
     * 出入库记录列表
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param query
     * @return
     */
    @GetMapping("/page")
    public ResultJson<PageVo<StockRecordVo>> page(@RequestHeader long merchantNo,
                                                  @RequestHeader long storeNo,
                                                  @RequestHeader String accountId,
                                                  StockRecordQuery query) {
        return ResultJson.ok(stockRecordService.page(merchantNo, storeNo, accountId, query));
    }
}
