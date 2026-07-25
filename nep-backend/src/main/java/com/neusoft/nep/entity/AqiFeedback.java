package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aqi_feedback")
public class AqiFeedback {
    @TableId(type = IdType.AUTO)
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
    private Integer state;
    private String remarks;
}
