package com.neusoft.nep.dto;

import lombok.Data;

/**
 * 监督员注册入参（前端字段保持 phone/realName/birthDate/gender）
 */
@Data
public class SupervisorRegisterDTO {
    private String phone;
    private String password;
    private String realName;
    private String birthDate;
    private String gender;
}
