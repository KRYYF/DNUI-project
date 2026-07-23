package com.neusoft.nep.service;

import com.neusoft.nep.entity.Aqi;

import java.util.List;

/**
 * AQI 等级业务
 */
public interface AqiService {

    List<Aqi> listLevels();
}
