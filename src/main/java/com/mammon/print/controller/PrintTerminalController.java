package com.mammon.print.controller;

import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.print.domain.dto.PrintTerminalDto;
import com.mammon.print.domain.dto.PrintTerminalQuery;
import com.mammon.print.domain.vo.PrintTerminalVo;
import com.mammon.print.service.PrintRecordService;
import com.mammon.print.service.PrintTerminalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/print/terminal")
public class PrintTerminalController {

    @Resource
    private PrintTerminalService printTerminalService;

    @Resource
    private PrintRecordService printRecordService;

    /**
     * 添加设备
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
                                   @RequestBody PrintTerminalDto dto) {
        printTerminalService.save(merchantNo, storeNo, dto);
        return ResultJson.ok();
    }

    /**
     * 修改设备
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody PrintTerminalDto dto) {
        printTerminalService.edit(merchantNo, storeNo, id, dto);
        return ResultJson.ok();
    }

    /**
     * 删除设备
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        printTerminalService.delete(id);
        return ResultJson.ok();
    }

    /**
     * 重启、关闭设备
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @param resetType  0:关闭，1:重启
     * @return
     */
    @PutMapping("/reset/{id}")
    public ResultJson<Void> restart(@RequestHeader long merchantNo,
                                    @RequestHeader long storeNo,
                                    @RequestHeader String accountId,
                                    @PathVariable("id") String id,
                                    @RequestParam int resetType) {
        printTerminalService.restart(id, resetType);
        return ResultJson.ok();
    }

    /**
     * 获取设备链接状态
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param id
     * @return
     */
    @GetMapping("/connect-status/{id}")
    public ResultJson<Integer> connectStatus(@RequestHeader long merchantNo,
                                             @RequestHeader long storeNo,
                                             @RequestHeader String accountId,
                                             @PathVariable("id") String id) {
        return ResultJson.ok(printTerminalService.findConnectStatus(id));
    }

    @GetMapping("/{id}")
    public ResultJson<PrintTerminalVo> info(@RequestHeader long merchantNo,
                                            @RequestHeader long storeNo,
                                            @RequestHeader String accountId,
                                            @PathVariable("id") String id) {
        PrintTerminalVo vo = printTerminalService.info(id);
        return ResultJson.ok(vo);
    }

    @GetMapping("/page")
    public ResultJson<PageVo<PrintTerminalVo>> page(@RequestHeader long merchantNo,
                                                    @RequestHeader long storeNo,
                                                    @RequestHeader String accountId,
                                                    PrintTerminalQuery dto) {
        PageVo<PrintTerminalVo> result = printTerminalService.page(merchantNo, storeNo, dto);
        return ResultJson.ok(result);
    }

    /**
     * 测试打印
     *
     * @return
     */
    @PostMapping("/test/{id}")
    public ResultJson<Void> test(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id) {
        printRecordService.test(merchantNo, storeNo, accountId, id);
        return ResultJson.ok();
    }
}
