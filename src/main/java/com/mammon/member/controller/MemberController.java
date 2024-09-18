package com.mammon.member.controller;

import com.mammon.common.PageResult;
import com.mammon.common.PageVo;
import com.mammon.common.ResultJson;
import com.mammon.member.domain.dto.MemberDto;
import com.mammon.member.domain.query.MemberQuery;
import com.mammon.member.domain.vo.MemberInfoVo;
import com.mammon.member.domain.vo.MemberSummaryCashierVo;
import com.mammon.member.service.MemberService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping
    public ResultJson<Void> create(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @RequestBody MemberDto dto) {
        memberService.create(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PostMapping("/import")
    public ResultJson<Void> memberImport(@RequestHeader long merchantNo,
                                         @RequestHeader long storeNo,
                                         @RequestHeader String accountId,
                                         @RequestParam("file") MultipartFile file) {
        memberService.memberImport(merchantNo, storeNo, accountId, file);
        return ResultJson.ok();
    }

    @PutMapping("/{id}")
    public ResultJson<Void> edit(@RequestHeader long merchantNo,
                                 @RequestHeader long storeNo,
                                 @RequestHeader String accountId,
                                 @PathVariable("id") String id,
                                 @RequestBody MemberDto dto) {
        memberService.edit(merchantNo, storeNo, accountId, id, dto);
        return ResultJson.ok();
    }

    @PutMapping("/status/{id}")
    public ResultJson<Void> editStatus(@RequestHeader long merchantNo,
                                       @RequestHeader long storeNo,
                                       @RequestHeader String accountId,
                                       @PathVariable("id") String id,
                                       @RequestParam Integer status) {
        memberService.editStatus(merchantNo, storeNo, accountId, id, status);
        return ResultJson.ok();
    }

    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@RequestHeader long merchantNo,
                                   @RequestHeader long storeNo,
                                   @RequestHeader String accountId,
                                   @PathVariable("id") String id) {
        memberService.delete(merchantNo, id);
        return ResultJson.ok();
    }

    @GetMapping("/{id}")
    public ResultJson<MemberInfoVo> findById(@RequestHeader long merchantNo,
                                             @RequestHeader long storeNo,
                                             @RequestHeader String accountId,
                                             @PathVariable("id") String id) {
        return ResultJson.ok(memberService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson<PageVo<MemberInfoVo>> page(@RequestHeader long merchantNo,
                                                 @RequestHeader long storeNo,
                                                 @RequestHeader String accountId,
                                                 MemberQuery query) {
        PageVo<MemberInfoVo> result = memberService.page(merchantNo, query);
        return ResultJson.ok(result);
    }

    @GetMapping("/summary-cashier/{id}")
    public ResultJson<MemberSummaryCashierVo> summaryCashier(@RequestHeader long merchantNo,
                                                             @RequestHeader long storeNo,
                                                             @RequestHeader String accountId,
                                                             @PathVariable("id") String id) {
        return ResultJson.ok(memberService.summaryCashier(merchantNo, id));
    }
}