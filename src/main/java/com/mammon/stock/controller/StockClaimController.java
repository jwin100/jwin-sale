package com.mammon.stock.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.stock.domain.dto.StockClaimMakeDto;
import com.mammon.stock.domain.vo.StockClaimAutomaticVo;
import com.mammon.stock.domain.vo.StockClaimDetailVo;
import com.mammon.stock.domain.vo.StockClaimPageVo;
import com.mammon.stock.domain.dto.StockClaimAutomaticDto;
import com.mammon.stock.domain.dto.StockClaimDto;
import com.mammon.stock.domain.dto.StockClaimSplitDto;
import com.mammon.stock.domain.query.StockClaimPageQuery;
import com.mammon.stock.service.StockClaimService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 组装拆包
 *
 * @author dcl
 * @since 2024/3/12 15:10
 */
@RestController
@RequestMapping("/stock/claim")
public class StockClaimController {

    @Resource
    private StockClaimService stockClaimService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody StockClaimDto dto) {
        stockClaimService.save(merchantNo, dto);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody StockClaimDto dto) {
        stockClaimService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam int status) {
        stockClaimService.editStatus(id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        stockClaimService.deleteById(id);
        return ResultJson.ok();
    }

    /**
     * 组装
     *
     * @return
     */
    @PutMapping("/make")
    public ResultJson<Void> make(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @RequestBody StockClaimMakeDto dto) {
        stockClaimService.make(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 拆包
     *
     * @return
     */
    @PutMapping("/split")
    public ResultJson<Void> split(@RequestHeader long merchantNo,
                                  @RequestHeader long storeNo,
                                  @RequestHeader String accountId,
                                  @RequestBody StockClaimSplitDto dto) {
        stockClaimService.split(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    /**
     * 自动组装拆包
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PutMapping("/automatic")
    public ResultJson<StockClaimAutomaticVo> automatic(@RequestHeader long merchantNo,
                                                       @RequestHeader long storeNo,
                                                       @RequestHeader String accountId,
                                                       @RequestBody StockClaimAutomaticDto dto) {
        StockClaimAutomaticVo vo = stockClaimService.automatic(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok(vo);
    }

    @GetMapping("/{id}")
    public ResultJson<StockClaimDetailVo> detail(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 @PathVariable("id") String id) {
        StockClaimDetailVo detailVo = stockClaimService.findDetailById(id);
        return ResultJson.ok(detailVo);
    }


    @GetMapping("/page")
    public ResultJson<PageVo<StockClaimPageVo>> page(@RequestHeader long merchantNo,
                                                     @RequestHeader long storeNo,
                                                     @RequestHeader String accountId,
                                                     StockClaimPageQuery query) {
        PageVo<StockClaimPageVo> pageVo = stockClaimService.page(merchantNo, query);
        return ResultJson.ok(pageVo);
    }
}
