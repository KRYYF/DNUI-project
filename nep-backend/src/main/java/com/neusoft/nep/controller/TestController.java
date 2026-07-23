package com.neusoft.nep.controller;

import com.neusoft.nep.common.R;
import com.neusoft.nep.mapper.AqiMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工程连通性测试接口
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final AqiMapper aqiMapper;

    public TestController(AqiMapper aqiMapper) {
        this.aqiMapper = aqiMapper;
    }

    /**
     * 验证服务能否启动
     */
    @GetMapping("/hello")
    public R hello() {
        return R.success("hello nep");
    }

    /**
     * 验证数据库连接：查询 aqi 表记录数
     */
    @GetMapping("/db")
    public R db() {
        Long count = aqiMapper.selectCount(null);
        return R.success("aqi 表共 " + count + " 条");
    }
}
