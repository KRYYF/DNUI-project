package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 市级行政区域实体
 */
@Data
@TableName("grid_city")
public class GridCity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String cityName;

    private String cityCode;

    private Integer provinceId;

    /** 是否 106 大城市：1 是 / 0 否 */
    private Integer isMajorCity;

    @TableLogic
    private Integer deleted;
}
