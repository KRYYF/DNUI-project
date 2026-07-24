package com.neusoft.nep.service;

import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.vo.FeedbackListVO;

import java.util.List;

/**
 * 反馈业务
 */
public interface AqiFeedbackService {

    void submit(FeedbackSubmitDTO dto);

    /**
     * @param telId 监督员手机号（前端字段名仍传 supervisorId）
     */
    List<FeedbackListVO> myList(String telId);
}
