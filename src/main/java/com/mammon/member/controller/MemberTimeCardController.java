package com.mammon.member.controller;

import com.mammon.common.ResultJson;
import com.mammon.member.service.MemberTimeCardService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dcl
 * @since 2024/8/8 11:51
 */
@RequestMapping("/member/time-card")
public class MemberTimeCardController {

    private final MemberTimeCardService memberTimeCardService;

    public MemberTimeCardController(MemberTimeCardService memberTimeCardService) {
        this.memberTimeCardService = memberTimeCardService;
    }

    /**
     * 删除计次卡
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultJson<Void> delete(@PathVariable String id) {
        memberTimeCardService.deleteById(id);
        return ResultJson.ok();
    }
}
