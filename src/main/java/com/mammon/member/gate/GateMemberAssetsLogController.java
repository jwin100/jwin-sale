package com.mammon.member.gate;

import com.mammon.common.ResultJson;
import com.mammon.member.domain.enums.MemberAssetsLogType;
import com.mammon.member.domain.query.MemberAssetsLogQuery;
import com.mammon.member.service.MemberAssetsLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2023-04-04 14:45:50
 */
@RestController
@RequestMapping("/gate/member/assets-log")
public class GateMemberAssetsLogController {

    // 储值余额变更记录和积分变更记录
    @Resource
    private MemberAssetsLogService memberAssetsLogService;

    /**
     * 日志记录
     *
     * @param merchantNo
     * @param storeNo
     * @param accountId
     * @return
     */
    @PostMapping("/integral/page")
    public ResultJson integralLogPage(@RequestHeader long merchantNo,
                                      @RequestHeader long storeNo,
                                      @RequestHeader String accountId,
                                      @RequestBody MemberAssetsLogQuery query) {
        query.setType(MemberAssetsLogType.CHANGE_INTEGRAL.getCode());
        return ResultJson.ok(memberAssetsLogService.page(query));
    }
}
