package com.mammon.chain.controller;

import com.mammon.chain.service.ChainRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dcl
 * @since 2024/10/17 13:14
 */
@RestController
@RequestMapping
public class ChainRedirectController {

    @Resource
    private ChainRecordService chainRecordService;

    /**
     * 保存短链接打开记录并重定向到目标链接
     * <p>
     * 要加防刷机制
     *
     * @param code
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/{code}")
    public void redirect(@PathVariable("code") String code,
                         HttpServletRequest request, HttpServletResponse response) {
        chainRecordService.redirect(code, request, response);
    }
}
