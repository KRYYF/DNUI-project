package com.neusoft.nep.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 我的反馈列表项
 */
@Data
public class FeedbackListVO {
    private Integer id;
    private String provinceName;
    private String cityName;
    private String detailAddress;
    private Integer estimatedLevel;
    private String feedbackDesc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime feedbackTime;
    private String status;
}
