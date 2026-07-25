package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.service.AqiFeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public R myList(@RequestParam String supervisorId) {
        return R.success(aqiFeedbackService.myList(supervisorId));
    }

    @GetMapping("/pageQuery")
    public R pageQuery(@RequestParam Map<String, Object> params) {
        return R.success(aqiFeedbackService.pageQuery(params));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Integer id) {
        return R.success(aqiFeedbackService.detail(id));
    }

    @PostMapping("/assign")
    public R assign(@RequestBody Map<String, Integer> params) {
        Integer feedbackId = params.get("feedbackId");
        Integer gmId = params.get("gmId");
        aqiFeedbackService.assign(feedbackId, gmId);
        return R.success();
    }
}
