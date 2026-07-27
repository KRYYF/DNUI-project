package com.neusoft.nep.service;

import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.vo.FeedbackListVO;
import com.neusoft.nep.vo.FeedbackDetailVO;
import com.neusoft.nep.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface AqiFeedbackService {

    void submit(FeedbackSubmitDTO dto);

    List<FeedbackListVO> myList(String telId);

    PageVO<FeedbackListVO> pageQuery(Map<String, Object> params);

    FeedbackDetailVO detail(Integer id);

    void assign(Integer feedbackId, Integer gmId);
}
