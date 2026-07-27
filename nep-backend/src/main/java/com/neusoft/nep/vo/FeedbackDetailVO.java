package com.neusoft.nep.vo;

import lombok.Data;

@Data
public class FeedbackDetailVO {
    private Integer id;
    private String supervisorId;
    private String supervisorName;
    private Integer provinceId;
    private String provinceName;
    private Integer cityId;
    private String cityName;
    private String address;
    private Integer estimatedLevel;
    private String feedbackDesc;
    private String feedbackTime;
    private Integer state;
    private String stateText;
    private Integer gmId;
    private String gridMemberName;
    private String assignTime;
    private Integer so2Value;
    private Integer so2Level;
    private Integer coValue;
    private Integer coLevel;
    private Integer spmValue;
    private Integer spmLevel;
    private Integer aqiLevel;
}
