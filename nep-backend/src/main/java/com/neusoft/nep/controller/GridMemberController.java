package com.neusoft.nep.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.common.R;
import com.neusoft.nep.dto.GridMemberAqiSubmitDTO;
import com.neusoft.nep.dto.GridMemberLoginDTO;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.service.GridMemberService;
import com.neusoft.nep.vo.GridFeedbackDetailVO;
import com.neusoft.nep.vo.GridFeedbackVO;
import com.neusoft.nep.vo.GridMemberLoginVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网格员控制器
 *
 * PR1：
 * 统一 /api/gridMember/** 路径空间
 *
 * PR2：
 * 增加网格员登录、任务查询、任务详情、AQI填报
 */
@RestController
@RequestMapping("/api/gridMember")
public class GridMemberController {


    private final GridMemberService gridMemberService;


    public GridMemberController(GridMemberService gridMemberService) {
        this.gridMemberService = gridMemberService;
    }



    /**
     * 网格员登录
     *
     * POST /api/gridMember/login
     */
    @PostMapping("/login")
    public R login(
            @RequestBody GridMemberLoginDTO dto
    ) {

        GridMemberLoginVO vo =
                gridMemberService.login(dto);

        return R.success(vo);
    }



    /**
     * 根据区域查询网格员
     *
     * PR1 保留接口
     */
    @GetMapping("/byRegion")
    public R byRegion(
            @RequestParam(required = false) Integer provinceId,
            @RequestParam(required = false) Integer cityId
    ) {

        List<GridMember> list =
                gridMemberService.getByRegion(
                        provinceId,
                        cityId
                );

        return R.success(list);
    }



    /**
     * 查询全部网格员
     *
     * PR1 保留接口
     */
    @GetMapping("/list")
    public R list() {

        return R.success(
                gridMemberService.getAll()
        );
    }



    /**
     * 根据id查询网格员
     *
     * PR1 保留接口
     */
    @GetMapping("/{gmId}")
    public R getById(
            @PathVariable Integer gmId
    ) {

        return R.success(
                gridMemberService.getById(gmId)
        );
    }



    /**
     * 查询当前网格员任务列表
     *
     * Header:
     * Authorization: token
     *
     * GET /api/gridMember/tasks
     */
    @GetMapping("/tasks")
    public R tasks(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        Page<GridFeedbackVO> page =
                gridMemberService.pageTasks(
                        token,
                        current,
                        size
                );

        return R.success(page);
    }



    /**
     * 查询任务详情
     *
     * GET /api/gridMember/detail/{afId}
     */
    @GetMapping("/detail/{afId}")
    public R detail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer afId
    ) {


        GridFeedbackDetailVO vo =
                gridMemberService.detail(
                        token,
                        afId
                );


        return R.success(vo);
    }




    /**
     * AQI反馈提交
     *
     * POST /api/gridMember/submit
     */
    @PostMapping("/submit")
    public R submit(
            @RequestHeader("Authorization") String token,
            @RequestBody GridMemberAqiSubmitDTO dto
    ) {

        gridMemberService.submitAqi(
                token,
                dto
        );

        return R.success();
    }

}