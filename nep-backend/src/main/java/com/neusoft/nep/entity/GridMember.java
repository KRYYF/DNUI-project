package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 网格员（官方 nep.sql）
 */
@Data
@TableName("grid_member")
public class GridMember {

    @TableId(value = "gm_id", type = IdType.AUTO)
    private Integer gmId;

    private String gmName;

    private String gmCode;

    private String password;

    private Integer provinceId;

    private Integer cityId;

    private String tel;

    /** 0 工作 / 1 非工作 / 2 其它 */
    private Integer state;

    private String remarks;
}
