package com.mammon.print.controller;

import com.mammon.common.ResultJson;
import com.mammon.print.domain.dto.PrintRecordSendDto;
import com.mammon.print.service.PrintRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/print")
public class PrintController {

    @Resource
    public PrintRecordService printRecordService;

    @PostMapping("/send")
    public ResultJson send(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @Validated @RequestBody PrintRecordSendDto dto) {
        printRecordService.send(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }
}
