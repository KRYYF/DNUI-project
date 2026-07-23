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
 * 网格员实体
 */
@Data
@TableName("grid_member")
public class GridMember {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String loginCode;

    private String password;

    private String realName;

    private String phone;

    private Integer provinceId;

    private Integer cityId;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
