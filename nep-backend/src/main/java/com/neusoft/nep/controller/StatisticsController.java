package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/provinceExceed")
    public R provinceExceed() {
        return R.success(statisticsService.getProvinceExceed());
    }

    @GetMapping("/aqiDistribution")
    public R aqiDistribution() {
        return R.success(statisticsService.getAqiDistribution());
    }

    @GetMapping("/aqiTrend")
    public R aqiTrend() {
        return R.success(statisticsService.getAqiTrend());
    }

    @GetMapping("/gridCoverage")
    public R gridCoverage() {
        return R.success(statisticsService.getGridCoverage());
    }

    @GetMapping("/realTimeCount")
    public R realTimeCount() {
        return R.success(statisticsService.getRealTimeCount());
    }

    @GetMapping("/confirmedPageQuery")
    public R confirmedPageQuery(@RequestParam Map<String, Object> params) {
        return R.success(statisticsService.confirmedPageQuery(params));
    }

    @GetMapping("/confirmedDetail/{id}")
    public R confirmedDetail(@PathVariable Integer id) {
        return R.success(statisticsService.confirmedDetail(id));
    }
}
