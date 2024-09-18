package com.mammon.sms.channel.fish;

import com.mammon.sms.channel.fish.model.dto.FishBatchSendDto;
import com.mammon.sms.channel.fish.model.dto.FishSendDto;
import com.mammon.sms.channel.fish.model.vo.FishSendResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "fish", url = "https://api.aiofish.com/rest/sms")
public interface FishClient {

    @PostMapping("/sendSms")
    FishSendResultVo send(@RequestBody FishSendDto dto);

    @PostMapping("/batchSendSms")
    FishSendResultVo batchSend(@RequestBody FishBatchSendDto dto);
}
