package com.mammon.sms.controller;

import com.mammon.common.ResultJson;
import com.mammon.sms.domain.dto.SmsTemplateSettingDto;
import com.mammon.sms.domain.vo.SmsTemplateSettingVo;
import com.mammon.sms.service.SmsTemplateSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dcl
 * @since 2024/5/9 13:53
 */
@RestController
@RequestMapping("/sms/template-setting")
public class SmsTemplateSettingController {

    @Resource
    private SmsTemplateSettingService smsTemplateSettingService;

    /**
     * 短信模板通知设置
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param tempType
     * @param dto
     * @return
     */
    @PutMapping("/temp-id/{tempType}")
    public ResultJson<Void> editTempId(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("tempType") int tempType,
                                       @RequestParam String tempId) {
        smsTemplateSettingService.editTempId(merchantNo, tempType, tempId);
        return ResultJson.ok();
    }

    @PutMapping("/temp-status/{tempType}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("tempType") int tempType,
                                       @RequestParam int status) {
        smsTemplateSettingService.editTempStatus(merchantNo, tempType, status);
        return ResultJson.ok();
    }

    @GetMapping("/list")
    public ResultJson<List<SmsTemplateSettingVo>> list(@RequestHeader long merchantNo,
                                                       @RequestHeader long storeNo,
                                                       @RequestHeader String accountId) {
        return ResultJson.ok(smsTemplateSettingService.list(merchantNo));
    }
}
