package com.mammon.member.controller;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberTagDto;
import com.mammon.member.domain.entity.MemberTagEntity;
import com.mammon.member.service.MemberTagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/member/tag")
public class MemberTagController {

    @Resource
    private MemberTagService memberTagService;

    @PostMapping
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @Validated @RequestBody MemberTagDto dto) {
        MemberTagEntity entity = memberTagService.create(merchantNo, dto);
        return ResultJson.ok(entity);
    }

    @PutMapping("/{id}")
    public ResultJson edit(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           @PathVariable("id") String id,
                           @Validated @RequestBody MemberTagDto dto) {
        memberTagService.edit(merchantNo, id, dto);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson delete(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @PathVariable("id") String id) {
        memberTagService.delete(merchantNo, id);
        return ResultJson.ok();
    }
}
