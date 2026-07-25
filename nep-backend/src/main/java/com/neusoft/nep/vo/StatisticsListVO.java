package com.neusoft.nep.vo;

import lombok.Data;

/**
 * 确认AQI数据列表项（管理员端 - 查看网格员确认AQI信息列表）
 */
@Data
public class StatisticsListVO {
    private Integer id;
    private Integer provinceId;
    private String provinceName;
    private Integer cityId;
    private String cityName;
    private String address;
    private Integer so2Value;
    private Integer so2Level;
    private Integer coValue;
    private Integer coLevel;
    private Integer spmValue;
    private Integer spmLevel;
    private Integer aqiLevel;
    private String confirmDate;
    private String confirmTime;
    private Integer gmId;
    private String gridMemberName;
}
