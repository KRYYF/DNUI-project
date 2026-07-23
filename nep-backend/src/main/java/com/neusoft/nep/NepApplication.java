package com.neusoft.nep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 东软环保公众监督系统后端启动类
 */
@SpringBootApplication
@MapperScan("com.neusoft.nep.mapper")
public class NepApplication {

    public static void main(String[] args) {
        SpringApplication.run(NepApplication.class, args);
    }
}
