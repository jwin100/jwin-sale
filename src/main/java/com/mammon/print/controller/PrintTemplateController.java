package com.mammon.print.controller;

import com.mammon.common.ResultJson;
import com.mammon.print.domain.dto.PrintTemplateEditDto;
import com.mammon.print.domain.query.PrintTemplateQuery;
import com.mammon.print.domain.vo.PrintTemplateListVo;
import com.mammon.print.domain.vo.PrintTemplateVo;
import com.mammon.print.service.PrintTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/print/template")
public class PrintTemplateController {

    @Resource
    private PrintTemplateService printTemplateService;

    /**
     * 编辑模板
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param dto
     * @return
     */
    @PutMapping
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @RequestBody PrintTemplateEditDto dto) {
        printTemplateService.edit(merchantNo, storeNo, dto);
        return ResultJson.ok();
    }

    @GetMapping("/{type}")
    public ResultJson<PrintTemplateVo> info(@RequestHeader long merchantNo,
                                            @RequestHeader long storeNo,
                                            @RequestHeader String accountId,
                                            @PathVariable("type") int type) {
        PrintTemplateVo template = printTemplateService.findDefaultByType(merchantNo, type);
        return ResultJson.ok(template);
    }

    @GetMapping("/list")
    public ResultJson<List<PrintTemplateListVo>> list(@RequestHeader long merchantNo,
                                                      @RequestHeader long storeNo,
                                                      @RequestHeader String accountId,
                                                      PrintTemplateQuery query) {
        List<PrintTemplateListVo> list = printTemplateService.getList(merchantNo, storeNo, query);
        return ResultJson.ok(list);
    }
}
