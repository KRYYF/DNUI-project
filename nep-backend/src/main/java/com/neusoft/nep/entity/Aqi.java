package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * AQI 等级实体
 */
@Data
@TableName("aqi")
public class Aqi {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** AQI 等级 1-6 */
    private Integer level;

    /** 优/良/轻度污染/... */
    private String grade;

    private String description;

    /** 前端展示颜色 */
    private String color;

    @TableLogic
    private Integer deleted;
}
