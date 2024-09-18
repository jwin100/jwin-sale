package com.mammon.print.channel.elind;

import com.mammon.print.channel.elind.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "elind", url = "https://open-api.10ss.net")
public interface ElindClient {
    /**
     * 添加终端授权
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/printer/addprinter")
    ElindResultJson<Void> addPrint(@RequestBody ElindAddPrintDto dto);

    /**
     * 删除终端授权
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/printer/deleteprinter")
    ElindResultJson<Void> deletePrint(@RequestBody ElindDeletePrintDto dto);

    /**
     * 获取打印机当前状态
     *
     * @param dto
     * @return
     */
    @PostMapping("/printer/getprintstatus")
    ElindResultJson<ElindStatusVo> getPrintStatus(@RequestBody ElindStatusDto dto);

    /**
     * 关机、重启打印机
     *
     * @param dto
     * @return
     */
    @PostMapping("/printer/shutdownrestart")
    ElindResultJson<Void> resetPrint(@RequestBody ElindResetPrintDto dto);

    @PostMapping("/printer/printinfo")
    ElindResultJson<ElindPrintInfoVo> printInfo(@RequestBody ElindPrintInfoDto dto);

    /**
     * 发起打印
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/print/index")
    ElindResultJson<Void> printIndex(@RequestBody ElindPrintDto dto);
}
