package com.neusoft.nep.dto;

import lombok.Data;

/**
 * 提交反馈入参（supervisorId 实际为手机号 telId）
 */
@Data
public class FeedbackSubmitDTO {
    /** 监督员手机号（兼容前端字段名 supervisorId） */
    private String supervisorId;
    private Integer provinceId;
    private Integer cityId;
    private String detailAddress;
    private Integer estimatedLevel;
    private String feedbackDesc;
}
