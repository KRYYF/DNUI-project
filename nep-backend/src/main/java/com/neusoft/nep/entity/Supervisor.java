package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 公众监督员（官方 nep.sql）
 */
@Data
@TableName("supervisor")
public class Supervisor {

    /** 手机号，主键 */
    @TableId(value = "tel_id", type = IdType.INPUT)
    private String telId;

    private String password;

    private String realName;

    private String birthday;

    /** 1 男 / 0 女 */
    private Integer sex;

    private String remarks;
}
