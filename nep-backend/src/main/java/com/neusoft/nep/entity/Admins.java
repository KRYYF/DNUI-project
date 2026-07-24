package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统管理员（官方 nep.sql）
 */
@Data
@TableName("admins")
public class Admins {

    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    private String adminCode;

    private String password;

    private String remarks;
}
