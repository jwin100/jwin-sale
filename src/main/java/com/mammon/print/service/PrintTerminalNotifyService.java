package com.mammon.print.service;

import com.mammon.print.channel.factory.BasePrintChannel;
import com.mammon.print.channel.factory.PrintChannelFactory;
import com.mammon.print.domain.entity.PrintChannelEntity;
import com.mammon.print.domain.model.PrintTerminalStatusModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author dcl
 * @since 2024/2/27 14:15
 */
@Service
public class PrintTerminalNotifyService {

    @Resource
    private PrintChannelService printChannelService;

    @Resource
    private PrintTerminalService printTerminalService;

    public Object terminalStatusNotify(String channelCode, HttpServletRequest request) {
        BasePrintChannel factory = PrintChannelFactory.get(channelCode);
        if (factory == null) {
            return null;
        }
        PrintChannelEntity channel = printChannelService.findByChannelCode(channelCode);
        if (channel == null) {
            return factory.responseFail();
        }
        PrintTerminalStatusModel model = factory.printStatusNotify(channel.getConfigStr(), request);
        if (model == null) {
            // 请求无参数返回
            return factory.responseSuccess();
        }
        printTerminalService.editStatus(model.getTerminalCode(), model.getStatus());
        return factory.responseSuccess();
    }
}
