package com.mammon.feedback.open;

import com.mammon.common.ResultJson;
import com.mammon.feedback.domain.dto.FeedbackDto;
import com.mammon.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dcl
 * @date 2022-11-10 11:24:57
 */
@RestController
@RequestMapping("/open/feedback")
public class OpenFeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 不需要登录
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    public ResultJson create(@RequestBody FeedbackDto dto) {
        feedbackService.save(0, 0, "", dto);
        return ResultJson.ok();
    }
}
