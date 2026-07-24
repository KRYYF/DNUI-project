package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 公众监督反馈（官方 nep.sql）
 */
@Data
@TableName("aqi_feedback")
public class AqiFeedback {

    @TableId(value = "af_id", type = IdType.AUTO)
    private Integer afId;

    private String telId;

    private Integer provinceId;

    private Integer cityId;

    private String address;

    private String information;

    private Integer estimatedGrade;

    private String afDate;

    private String afTime;

    private Integer gmId;

    private String assignDate;

    private String assignTime;

    /** 0 未指派 / 1 已指派 / 2 已确认 */
    private Integer state;

    private String remarks;
}
