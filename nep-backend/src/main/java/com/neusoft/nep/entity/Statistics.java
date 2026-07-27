package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实测统计（官方 nep.sql）
 */
@Data
@TableName("statistics")
public class Statistics {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer provinceId;

    private Integer cityId;

    private String address;

    private Integer so2Value;

    private Integer so2Level;

    private Integer coValue;

    private Integer coLevel;

    private Integer spmValue;

    private Integer spmLevel;

    private Integer aqiId;

    private String confirmDate;

    private String confirmTime;

    private Integer gmId;

    private Integer afId;

    private String fdId;

    private String information;

    private String remarks;
}
