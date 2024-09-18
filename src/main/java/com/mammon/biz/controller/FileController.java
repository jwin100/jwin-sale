package com.mammon.biz.controller;

import com.mammon.common.ResultJson;
import com.mammon.biz.domain.UploadAuthVo;
import com.mammon.biz.service.FileService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/biz/file")
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/upload-auth/{type}")
    private ResultJson<UploadAuthVo> uploadAuth(@RequestHeader long merchantNo,
                                                @PathVariable("type") int type) {
        UploadAuthVo vo = fileService.uploadFormAuth(merchantNo, type);
        return ResultJson.ok(vo);
    }
}
