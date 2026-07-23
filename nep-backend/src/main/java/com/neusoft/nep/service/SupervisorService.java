package com.neusoft.nep.service;

import com.neusoft.nep.dto.SupervisorLoginDTO;
import com.neusoft.nep.dto.SupervisorRegisterDTO;

import java.util.Map;

/**
 * 公众监督员业务
 */
public interface SupervisorService {

    void register(SupervisorRegisterDTO dto);

    Map<String, Object> checkPhone(String phone);

    Map<String, Object> login(SupervisorLoginDTO dto);
}
