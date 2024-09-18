package com.mammon.member.controller;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberAttrDto;
import com.mammon.member.domain.entity.MemberAttrEntity;
import com.mammon.member.service.MemberAttrService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/member/attr")
public class MemberAttrController {

    @Resource
    private MemberAttrService memberAttrService;

    @PutMapping
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @RequestBody MemberAttrDto dto) {
        memberAttrService.edit(merchantNo, dto);
        return ResultJson.ok();
    }

    @GetMapping
    public ResultJson<MemberAttrEntity> info(@RequestHeader long merchantNo,
                                             @RequestHeader long storeNo,
                                             @RequestHeader String accountId) {
        MemberAttrEntity attr = memberAttrService.findByMerchantNo(merchantNo);
        return ResultJson.ok(attr);
    }
}
