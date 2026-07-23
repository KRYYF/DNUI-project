package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 实测统计数据实体
 */
@Data
@TableName("statistics")
public class Statistics {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer aqiFeedbackId;

    private Integer gridMemberId;

    private BigDecimal so2Concentration;

    private Integer so2Iaqi;

    private BigDecimal coConcentration;

    private Integer coIaqi;

    private BigDecimal pm25Concentration;

    private Integer pm25Iaqi;

    /** 综合 AQI（取三项最大） */
    private Integer totalAqi;

    /** 综合 AQI 等级 1-6 */
    private Integer totalLevel;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime confirmTime;

    @TableLogic
    private Integer deleted;
}
