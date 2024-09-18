package com.mammon.print.open;

import com.mammon.common.ResultJson;
import com.mammon.print.service.PrintTerminalNotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
    @RequestMapping("/open/print/terminal")
public class OpenPrintTerminalController {

    @Resource
    private PrintTerminalNotifyService printTerminalNotifyService;

    /**
     * 硬件在线状态推送
     *
     * @param channelCode
     * @param request
     * @return
     */
    @RequestMapping("/notify-status/{channelCode}")
    public Object terminalStatusNotify(@PathVariable("channelCode") String channelCode,
                                     HttpServletRequest request) {
       return printTerminalNotifyService.terminalStatusNotify(channelCode, request);
    }
}
