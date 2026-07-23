package com.neusoft.nep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 省级行政区域实体
 */
@Data
@TableName("grid_province")
public class GridProvince {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String provinceName;

    private String provinceCode;

    @TableLogic
    private Integer deleted;
}
