package com.mammon.print.gate;

import com.mammon.common.ResultJson;
import com.mammon.print.domain.vo.PrintChannelListVo;
import com.mammon.print.service.PrintChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/gate/print/channel")
public class GatePrintChannelController {

    @Resource
    private PrintChannelService printChannelService;

    @GetMapping("/list/{classify}")
    public ResultJson<List<PrintChannelListVo>> findListByClassify(@PathVariable("classify") Integer classify) {
        return ResultJson.ok(printChannelService.findListByClassify(classify));
    }

    @GetMapping("/form-config/{channelId}")
    public ResultJson<String> formConfig(@PathVariable("channelId") String channelId) {
        return ResultJson.ok(printChannelService.findFormConfig(channelId));
    }
}
