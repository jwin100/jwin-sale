package com.mammon.feedback.controller;

import com.mammon.common.ResultJson;
import com.mammon.feedback.domain.dto.FeedbackDto;
import com.mammon.feedback.domain.query.FeedbackPageQuery;
import com.mammon.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public ResultJson create(@RequestHeader long merchantNo,
                             @RequestHeader long storeNo,
                             @RequestHeader String accountId,
                             @RequestBody FeedbackDto dto) {
        feedbackService.save(merchantNo, storeNo, accountId, dto);
        return ResultJson.ok();
    }

    @PostMapping("/edit/{id}")
    public ResultJson edit(@PathVariable("id") String id,
                           @RequestBody FeedbackDto dto) {
        feedbackService.update(id, dto);
        return ResultJson.ok();
    }

    @GetMapping("/info/{id}")
    public ResultJson info(@PathVariable("id") String id) {
        return ResultJson.ok(feedbackService.findById(id));
    }

    @GetMapping("/page")
    public ResultJson page(@RequestHeader long merchantNo,
                           @RequestHeader long storeNo,
                           @RequestHeader String accountId,
                           FeedbackPageQuery query) {
        return ResultJson.ok(feedbackService.page(merchantNo, storeNo, accountId, query));
    }
}
