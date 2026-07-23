package com.neusoft.nep.dto;

import lombok.Data;

/**
 * 提交反馈入参
 */
@Data
public class FeedbackSubmitDTO {
    private Integer supervisorId;
    private Integer provinceId;
    private Integer cityId;
    private String detailAddress;
    private Integer estimatedLevel;
    private String feedbackDesc;
}
