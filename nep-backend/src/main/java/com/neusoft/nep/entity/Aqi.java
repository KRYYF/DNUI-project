package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * AQI 等级（官方 nep.sql）
 */
@Data
@TableName("aqi")
public class Aqi {

    @TableId(value = "aqi_id", type = IdType.AUTO)
    private Integer aqiId;

    private String chineseExplain;

    private String aqiExplain;

    private String color;

    private String healthImpact;

    private String takeSteps;

    private Integer so2Min;

    private Integer so2Max;

    private Integer coMin;

    private Integer coMax;

    private Integer spmMin;

    private Integer spmMax;

    private String remarks;
}
