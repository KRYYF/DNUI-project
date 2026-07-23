package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.service.AqiFeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公众监督反馈接口
 */
@RestController
@RequestMapping("/api/aqiFeedback")
public class AqiFeedbackController {

    private final AqiFeedbackService aqiFeedbackService;

    public AqiFeedbackController(AqiFeedbackService aqiFeedbackService) {
        this.aqiFeedbackService = aqiFeedbackService;
    }

    @PostMapping("/submit")
    public R submit(@RequestBody FeedbackSubmitDTO dto) {
        aqiFeedbackService.submit(dto);
        return R.success();
    }

    @GetMapping("/myList")
    public R myList(@RequestParam Integer supervisorId) {
        return R.success(aqiFeedbackService.myList(supervisorId));
    }
}
