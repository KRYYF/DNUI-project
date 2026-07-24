package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.service.RegionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 区域接口
 */
@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/provinces")
    public R provinces() {
        List<Map<String, Object>> data = regionService.listProvinces().stream().map(p -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", p.getProvinceId());
            item.put("provinceName", p.getProvinceName());
            return item;
        }).collect(Collectors.toList());
        return R.success(data);
    }

    @GetMapping("/cities/{provinceId}")
    public R cities(@PathVariable Integer provinceId) {
        List<Map<String, Object>> data = regionService.listCitiesByProvinceId(provinceId).stream().map(c -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getCityId());
            item.put("cityName", c.getCityName());
            item.put("provinceId", c.getProvinceId());
            return item;
        }).collect(Collectors.toList());
        return R.success(data);
    }
}
