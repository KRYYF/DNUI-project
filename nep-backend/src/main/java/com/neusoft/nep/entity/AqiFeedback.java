package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公众监督反馈实体
 */
@Data
@TableName("aqi_feedback")
public class AqiFeedback {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer supervisorId;

    private Integer provinceId;

    private Integer cityId;

    private String detailAddress;

    /** 预估 AQI 等级 1-6 */
    private Integer estimatedLevel;

    private String feedbackDesc;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime feedbackTime;

    /** 未指派/已指派/已完成 */
    private String status;

    private Integer assignedGridMemberId;

    /** 本地/异地 */
    private String assignType;

    @TableLogic
    private Integer deleted;
}
