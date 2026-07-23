package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.entity.Aqi;
import com.neusoft.nep.service.AqiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AQI 等级接口
 */
@RestController
@RequestMapping("/api/aqi")
public class AqiController {

    private final AqiService aqiService;

    public AqiController(AqiService aqiService) {
        this.aqiService = aqiService;
    }

    @GetMapping("/levels")
    public R levels() {
        List<Map<String, Object>> data = aqiService.listLevels().stream().map((Aqi a) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("level", a.getLevel());
            item.put("grade", a.getGrade());
            item.put("color", a.getColor());
            item.put("description", a.getDescription());
            return item;
        }).collect(Collectors.toList());
        return R.success(data);
    }
}
