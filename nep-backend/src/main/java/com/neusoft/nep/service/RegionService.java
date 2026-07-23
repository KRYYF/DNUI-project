package com.neusoft.nep.service;

import com.neusoft.nep.entity.GridCity;
import com.neusoft.nep.entity.GridProvince;

import java.util.List;

/**
 * 区域业务
 */
public interface RegionService {

    List<GridProvince> listProvinces();

    List<GridCity> listCitiesByProvinceId(Integer provinceId);
}
