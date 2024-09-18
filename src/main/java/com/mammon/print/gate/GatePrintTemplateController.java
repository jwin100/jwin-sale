package com.mammon.print.gate;

import com.mammon.common.ResultJson;
import com.mammon.print.domain.query.PrintTemplateQuery;
import com.mammon.print.domain.vo.PrintTemplateListVo;
import com.mammon.print.domain.vo.PrintTemplateTypeVo;
import com.mammon.print.domain.vo.PrintTemplateVo;
import com.mammon.print.domain.vo.PrintTemplateWidthVo;
import com.mammon.print.service.PrintTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/print/template")
public class GatePrintTemplateController {

    @Resource
    private PrintTemplateService printTemplateService;

    @GetMapping("/type-list/{classify}")
    public ResultJson<List<PrintTemplateTypeVo>> typeList(@RequestHeader long merchantNo,
                                                          @RequestHeader long storeNo,
                                                          @RequestHeader String accountId,
                                                          @PathVariable("classify") int classify) {
        List<PrintTemplateTypeVo> vos = printTemplateService.getTemplateTypeList(classify);
        return ResultJson.ok(vos);
    }

    @GetMapping("/width-list/{classify}")
    public ResultJson<List<PrintTemplateWidthVo>> widthList(@RequestHeader long merchantNo,
                                                            @RequestHeader long storeNo,
                                                            @RequestHeader String accountId,
                                                            @PathVariable("classify") int classify) {
        List<PrintTemplateWidthVo> vos = printTemplateService.getTemplateWidthList(classify);
        return ResultJson.ok(vos);
    }
}
