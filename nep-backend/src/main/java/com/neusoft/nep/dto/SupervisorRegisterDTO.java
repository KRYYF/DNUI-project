package com.neusoft.nep.dto;

import lombok.Data;

/**
 * 监督员注册入参
 */
@Data
public class SupervisorRegisterDTO {
    private String phone;
    private String password;
    private String realName;
    private String birthDate;
    private String gender;
}
