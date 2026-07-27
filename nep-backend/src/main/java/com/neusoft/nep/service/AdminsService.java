package com.neusoft.nep.service;

import java.util.Map;

public interface AdminsService {

    Map<String, Object> login(String adminCode, String password);
}
