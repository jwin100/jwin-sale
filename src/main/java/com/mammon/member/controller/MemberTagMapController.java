package com.mammon.member.controller;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberTagMapDto;
import com.mammon.member.service.MemberTagMapService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/member/tag-map")
public class MemberTagMapController {

    @Resource
    private MemberTagMapService memberTagMapService;

    @PostMapping
    private ResultJson create(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              @Validated @RequestBody MemberTagMapDto dto) {
        memberTagMapService.batchSave(dto.getMemberId(), dto.getTagIds());
        return ResultJson.ok();
    }

    @DeleteMapping("/{tagId}")
    private ResultJson delete(@RequestHeader long merchantNo,
                              @RequestHeader long storeNo,
                              @RequestHeader String accountId,
                              @PathVariable("tagId") String tagId,
                              @RequestParam String memberId) {
        memberTagMapService.deleteByTagId(memberId, tagId);
        return ResultJson.ok();
    }
}
