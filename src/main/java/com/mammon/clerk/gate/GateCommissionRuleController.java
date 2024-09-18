package com.mammon.clerk.gate;

import com.mammon.common.ResultJson;
import com.mammon.clerk.domain.enums.CommissionRuleMode;
import com.mammon.clerk.domain.vo.CommissionRuleModeVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @since 2024/4/8 15:05
 */
@RestController
@RequestMapping("/gate/clerk/commission-rule")
public class GateCommissionRuleController {

    @GetMapping("/model/list")
    public ResultJson<List<CommissionRuleModeVo>> getModeList() {
        List<CommissionRuleModeVo> list = Arrays.stream(CommissionRuleMode.values()).map(x -> {
            CommissionRuleModeVo vo = new CommissionRuleModeVo();
            vo.setCode(x.getCode());
            vo.setName(x.getName());
            vo.setType(x.getType().getCode());
            vo.setUnit(x.getUnit().getCode());
            vo.setUnitName(x.getUnit().getName());
            return vo;
        }).collect(Collectors.toList());
        return ResultJson.ok(list);
    }
}
