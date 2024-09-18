package com.mammon.print.channel.gain;

import com.mammon.print.channel.elind.model.*;
import com.mammon.print.channel.gain.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dcl
 * @since 2024/3/26 18:23
 */
@FeignClient(name = "gain", url = "https://api.poscom.cn/apisc")
public interface GainClient {

    /**
     * 添加打印机
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/adddev")
    GainBasePrintVo addPrint(@RequestBody GainAddPrintDto dto);

    /**
     * 修改打印机
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/editdev")
    GainBasePrintVo editPrint(@RequestBody GainAddPrintDto dto);

    /**
     * 删除打印机
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/deldev")
    GainBasePrintVo deletePrint(@RequestBody GainDeletePrintDto dto);

    /**
     * 获取打印机当前状态
     *
     * @param dto
     * @return
     */
    @PostMapping("/getStatus")
    GainStatusVo getPrintStatus(@RequestBody GainStatusDto dto);

    /**
     * 获取打印机信息
     *
     * @param dto
     * @return
     */
    @GetMapping("/device")
    GainPrintInfoVo printInfo(@SpringQueryMap GainPrintInfoDto dto);

    /**
     * 发起打印
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/sendMsg")
    GainBasePrintVo printIndex(@RequestBody GainPrintDto dto);
}
