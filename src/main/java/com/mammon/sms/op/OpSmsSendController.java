package com.mammon.sms.op;

import com.mammon.common.ResultJson;
import com.mammon.sms.domain.dto.SmsQuery;
import com.mammon.sms.domain.dto.SmsSendExamineDto;
import com.mammon.sms.domain.dto.SmsSendRecordDto;
import com.mammon.sms.service.SmsSendItemService;
import com.mammon.sms.service.SmsSendService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/op/sms/sms-send")
public class OpSmsSendController {

    @Resource
    private SmsSendService smsSendService;

    @Resource
    private SmsSendItemService smsSendItemService;

    @PostMapping("/send")
    public ResultJson send(@RequestHeader String accountId,
                           @RequestBody SmsSendRecordDto dto) {
        smsSendService.syncSend(0, 0, accountId, dto);
        return ResultJson.ok();
    }

    @GetMapping("/page")
    public ResultJson smsPage(SmsQuery dto) {
        return ResultJson.ok(smsSendService.page(0, 0, null, dto));
    }


    @PutMapping("/examine/{ids}")
    public ResultJson smsExamine(@PathVariable("ids") List<String> ids,
                                 @RequestBody SmsSendExamineDto dto) {
        smsSendService.smsExamine(ids, dto);
        return ResultJson.ok();
    }

    /**
     * @param smsId
     * @return
     */
    @GetMapping("/item-list")
    public ResultJson smsItems(@RequestParam String smsId) {
        return ResultJson.ok(smsSendItemService.findAllBySmsSendId(0, 0, null, smsId));
    }
}
