package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.dto.AdminLoginDTO;
import com.neusoft.nep.service.AdminsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminsController {

    private final AdminsService adminsService;

    public AdminsController(AdminsService adminsService) {
        this.adminsService = adminsService;
    }

    @PostMapping("/login")
    public R login(@RequestBody AdminLoginDTO dto) {
        return R.success(adminsService.login(dto.getAdminCode(), dto.getPassword()));
    }
}
