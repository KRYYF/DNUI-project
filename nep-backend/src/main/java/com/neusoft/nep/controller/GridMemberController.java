package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.service.GridMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网格员控制器（PR#1：统一 /api/gridMember/** 路径空间；
 * PR#2 计划扩展：登录 /login、任务列表 /tasks、实测提交 /submit）
 */
@RestController
@RequestMapping("/api/gridMember")
public class GridMemberController {

    private final GridMemberService gridMemberService;

    public GridMemberController(GridMemberService gridMemberService) {
        this.gridMemberService = gridMemberService;
    }

    @GetMapping("/byRegion")
    public R byRegion(@RequestParam(required = false) Integer provinceId,
                      @RequestParam(required = false) Integer cityId) {
        List<GridMember> list = gridMemberService.getByRegion(provinceId, cityId);
        return R.success(list);
    }

    @GetMapping("/list")
    public R list() {
        return R.success(gridMemberService.getAll());
    }

    @GetMapping("/{gmId}")
    public R getById(@PathVariable Integer gmId) {
        return R.success(gridMemberService.getById(gmId));
    }
}
