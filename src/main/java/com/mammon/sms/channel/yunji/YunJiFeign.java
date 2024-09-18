package com.mammon.sms.channel.yunji;

import com.mammon.sms.channel.yunji.model.dto.YunJiBatchSendDto;
import com.mammon.sms.channel.yunji.model.dto.YunJiSingleSendDto;
import com.mammon.sms.channel.yunji.model.vo.YunJiBatchSendVo;
import com.mammon.sms.channel.yunji.model.vo.YunJiSingleSendVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dcl
 * @date 2022-10-13 11:02:50
 */
@FeignClient(name = "yunJi", url = "https://market.juncdt.com/smartmarket")
public interface YunJiFeign {

    /**
     * 单条发送
     */
    @PostMapping("/msgService/sendMessage")
    YunJiSingleSendVo singleSend(@RequestBody YunJiSingleSendDto dto);

    /**
     * 批量发送
     */
    @PostMapping("/msgService/sendMessageToMulti")
    YunJiBatchSendVo batchSend(@RequestBody YunJiBatchSendDto dto);
}
