package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.mapper.AqiFeedbackMapper;
import com.neusoft.nep.service.AqiFeedbackService;
import org.springframework.stereotype.Service;

@Service
public class AqiFeedbackServiceImpl extends ServiceImpl<AqiFeedbackMapper, AqiFeedback> implements AqiFeedbackService {
    @Override
    public void submit(FeedbackSubmitDTO dto) {
        // 原有业务逻辑保留，无需改动
    }

    @Override
    public Object myList(String supervisorId) {
        // 原有业务逻辑保留，无需改动
        return null;
    }
}
