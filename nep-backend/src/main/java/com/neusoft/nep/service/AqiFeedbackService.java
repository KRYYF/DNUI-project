package com.neusoft.nep.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.dto.FeedbackSubmitDTO;

public interface AqiFeedbackService extends IService<AqiFeedback> {
    // 保留原有业务方法，兼容组长代码
    void submit(FeedbackSubmitDTO dto);
    Object myList(String supervisorId);
}
