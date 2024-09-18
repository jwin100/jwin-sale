package com.mammon.member.gate;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberTagDto;
import com.mammon.member.service.MemberTagService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gate/member/tag")
public class GateMemberTagController {

    @Resource
    private MemberTagService memberTagService;

    @GetMapping("/list")
    public ResultJson list(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId) {
        return ResultJson.ok(memberTagService.findAll(merchantNo));
    }
}
