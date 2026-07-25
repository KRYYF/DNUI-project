package com.neusoft.nep.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.common.R;
import com.neusoft.nep.entity.Aqi;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.entity.Statistics;
import com.neusoft.nep.mapper.AqiFeedbackMapper;
import com.neusoft.nep.mapper.AqiMapper;
import com.neusoft.nep.mapper.GridMemberMapper;
import com.neusoft.nep.service.AqiFeedbackService;
import com.neusoft.nep.service.StatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/grid")
@CrossOrigin
public class GridMemberController {

    @Resource
    private GridMemberMapper gridMemberMapper;
    @Resource
    private AqiFeedbackMapper aqiFeedbackMapper;
    @Resource
    private AqiFeedbackService aqiFeedbackService;
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private AqiMapper aqiMapper;

    // 网格员登录
    @PostMapping("/login")
    public R login(@RequestBody Map<String, Object> body) {
        String gmCode = body.get("gmCode").toString();
        String password = body.get("password").toString();
        LambdaQueryWrapper<GridMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GridMember::getGmCode, gmCode);
        wrapper.eq(GridMember::getPassword, password);
        GridMember member = gridMemberMapper.selectOne(wrapper);
        if (member == null) {
            return R.error("账号或密码错误");
        }
        return R.success(member);
    }

    // 查看指派给自己的任务列表
    @GetMapping("/task/list")
    public R getTaskList(@RequestParam Integer gmId,
                         @RequestParam Integer pageNum,
                         @RequestParam Integer pageSize) {
        Page<AqiFeedback> page = new Page<>(pageNum, pageSize);
        Page<AqiFeedback> data = aqiFeedbackMapper.selectTaskPage(page, gmId);
        return R.success(data);
    }

    // 任务详情
    @GetMapping("/task/{afId}")
    public R getTaskDetail(@PathVariable Integer afId) {
        AqiFeedback feedback = aqiFeedbackMapper.selectDetail(afId);
        return R.success(feedback);
    }

   /* // 前端输入污染物实时计算AQI等级（增加defaultValue，无参数不会500）
    @GetMapping("/calcAqi")
    public R calcAqi(@RequestParam(defaultValue = "0") Integer so2,
                     @RequestParam(defaultValue = "0") Integer co,
                     @RequestParam(defaultValue = "0") Integer spm) {
        Aqi matchedAqi = aqiMapper.matchAqiLevel(so2, co, spm);
        if (matchedAqi == null) {
            matchedAqi = new Aqi();
            matchedAqi.setAqiId(6);
            matchedAqi.setChineseExplain("六");
            matchedAqi.setAqiExplain("严重污染");
            matchedAqi.setColor("#7E0123");
        }
        return R.success(matchedAqi);
    }  */

    // 提交实测AQI数据，自动匹配aqi等级填充aqiId，修改工单state=2
    @PostMapping("/submit")
    public R submitData(@RequestBody Statistics statistics) {
        AqiFeedback task = aqiFeedbackMapper.selectDetail(Integer.valueOf(statistics.getFdId()));
        // 回填工单基础地址、描述信息
        statistics.setProvinceId(task.getProvinceId());
        statistics.setCityId(task.getCityId());
        statistics.setAddress(task.getAddress());
        statistics.setInformation(task.getInformation());

        LocalDateTime now = LocalDateTime.now();
        statistics.setConfirmDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        statistics.setConfirmTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));

        Integer so2Val = statistics.getSo2Value();
        Integer coVal = statistics.getCoValue();
        Integer spmVal = statistics.getSpmValue();

        Aqi matchedAqi = aqiMapper.matchAqiLevel(so2Val, coVal, spmVal);
        if (matchedAqi != null) {
            statistics.setAqiId(matchedAqi.getAqiId());
        } else {
            statistics.setAqiId(6);
        }

        statistics.setSo2Level(0);
        statistics.setCoLevel(0);
        statistics.setSpmLevel(0);

        statisticsService.save(statistics);
        task.setState(2);
        aqiFeedbackService.updateById(task);

        return R.success("实测数据提交成功");
    }
}
