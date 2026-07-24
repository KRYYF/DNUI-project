package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 市级区域（官方 nep.sql）
 */
@Data
@TableName("grid_city")
public class GridCity {

    @TableId(value = "city_id", type = IdType.AUTO)
    private Integer cityId;

    private String cityName;

    private Integer provinceId;

    private String remarks;
}
