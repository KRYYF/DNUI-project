package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grid_member")
public class GridMember {
    @TableId(type = IdType.AUTO)
    private Integer gmId;
    private String gmName;
    private String gmCode;
    private String password;
    private Integer provinceId;
    private Integer cityId;
    private String tel;
    private Integer state;
    private String remarks;
}
