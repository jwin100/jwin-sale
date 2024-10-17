package com.mammon.chain.controller;

import com.mammon.chain.service.ChainRecordService;
import com.mammon.chain.service.ChainService;
import com.mammon.common.ResultJson;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/chain")
public class ChainController {

    @Resource
    private ChainService chainService;
    
    /**
     * 创建短连接
     *
     * @param path 长连接
     * @return 短连接
     */
    @PostMapping
    public ResultJson<String> create(@RequestParam String path) {
        String shortUrl = chainService.create(path);
        return ResultJson.ok(shortUrl);
    }
}
