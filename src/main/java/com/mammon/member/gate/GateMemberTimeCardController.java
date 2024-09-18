package com.mammon.member.gate;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.entity.MemberTimeCardEntity;
import com.mammon.member.domain.vo.MemberTimeCardListVo;
import com.mammon.member.service.MemberTimeCardLogService;
import com.mammon.member.service.MemberTimeCardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/member/time-card")
public class GateMemberTimeCardController {

    @Resource
    private MemberTimeCardService memberTimeCardService;

    @Resource
    private MemberTimeCardLogService memberTimeCardLogService;

    /**
     * 获取当前商户、会员、商品可使用的计次卡信息
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param memberId
     * @return
     */
    @GetMapping("/list/spu-no/{memberId}")
    public ResultJson<List<MemberTimeCardEntity>> listBySpuNo(@RequestHeader long merchantNo,
                                                              @RequestHeader long storeNo,
                                                              @RequestHeader String accountId,
                                                              @PathVariable("memberId") String memberId,
                                                              @RequestParam String spuId) {
        return ResultJson.ok(memberTimeCardService.findAllByMemberIdAndSpuId(memberId, spuId));
    }

    /**
     * 计次卡信息
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @param memberId
     * @return
     */
    @GetMapping("/list/{memberId}")
    public ResultJson<List<MemberTimeCardListVo>> list(@RequestHeader long merchantNo,
                                                       @RequestHeader long storeNo,
                                                       @RequestHeader String accountId,
                                                       @PathVariable("memberId") String memberId) {
        return ResultJson.ok(memberTimeCardService.findAllByMemberId(memberId));
    }
}
