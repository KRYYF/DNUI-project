package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.dto.SupervisorLoginDTO;
import com.neusoft.nep.dto.SupervisorRegisterDTO;
import com.neusoft.nep.service.SupervisorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公众监督员接口
 */
@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    private final SupervisorService supervisorService;

    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @PostMapping("/register")
    public R register(@RequestBody SupervisorRegisterDTO dto) {
        supervisorService.register(dto);
        return R.success();
    }

    @GetMapping("/checkPhone")
    public R checkPhone(@RequestParam String phone) {
        return R.success(supervisorService.checkPhone(phone));
    }

    @PostMapping("/login")
    public R login(@RequestBody SupervisorLoginDTO dto) {
        return R.success(supervisorService.login(dto));
    }
}
