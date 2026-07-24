package com.neusoft.nep.vo;

import lombok.Data;

/**
 * 我的反馈列表项（对外字段名保持 NEPS 前端约定）
 */
@Data
public class FeedbackListVO {
    private Integer id;
    private String provinceName;
    private String cityName;
    private String detailAddress;
    private Integer estimatedLevel;
    private String feedbackDesc;
    private String feedbackTime;
    /** 未指派 / 已指派 / 已确认 */
    private String status;
}
