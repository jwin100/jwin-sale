package com.mammon.member.gate;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/member")
public class GateMemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/list")
    public ResultJson memberList(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId) {
        return ResultJson.ok(memberService.findAll(merchantNo));
    }

    @GetMapping("/search")
    public ResultJson memberSearch(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestParam String searchKey) {
        List<MemberInfoVo> members = memberService.memberSearch(merchantNo, searchKey);
        return ResultJson.ok(members);
    }
}