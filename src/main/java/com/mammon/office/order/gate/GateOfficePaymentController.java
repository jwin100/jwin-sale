package com.mammon.office.order.gate;

import com.mammon.common.ResultJson;
import com.mammon.office.order.domain.enums.OfficeOrderPayType;
import com.mammon.office.order.domain.vo.OfficePayTypeVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcl
 * @date 2023-03-03 10:44:07
 */
@RestController
@RequestMapping("/gate/office/payment")
public class GateOfficePaymentController {

    @GetMapping("/list")
    public ResultJson<List<OfficePayTypeVo>> list() {
        List<OfficePayTypeVo> vos = Arrays.stream(OfficeOrderPayType.values())
                .filter(OfficeOrderPayType::isStatus)
                .map(x -> {
                    OfficePayTypeVo vo = new OfficePayTypeVo();
                    vo.setCode(x.getCode());
                    vo.setName(x.getName());
                    vo.setIcon(x.getIcon());
                    return vo;
                }).collect(Collectors.toList());
        return ResultJson.ok(vos);
    }
}
