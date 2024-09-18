package com.mammon.member.controller;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberLevelDto;
import com.mammon.member.service.MemberLevelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/member/level")
public class MemberLevelController {

    @Resource
    private MemberLevelService memberLevelService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody List<MemberLevelDto> list) {
        memberLevelService.batchEdit(merchantNo, list);
        return ResultJson.ok();
    }
    
    @GetMapping
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(memberLevelService.findAllByMerchantNo(merchantNo));
    }
}
