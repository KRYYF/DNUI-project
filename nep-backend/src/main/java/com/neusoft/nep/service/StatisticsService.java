package com.neusoft.nep.service;

import com.neusoft.nep.vo.PageVO;
import com.neusoft.nep.vo.StatisticsDetailVO;
import com.neusoft.nep.vo.StatisticsListVO;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    List<Map<String, Object>> getProvinceExceed();

    List<Map<String, Object>> getAqiDistribution();

    Map<String, Object> getAqiTrend();

    Map<String, Object> getGridCoverage();

    Map<String, Object> getRealTimeCount();

    PageVO<StatisticsListVO> confirmedPageQuery(Map<String, Object> params);

    StatisticsDetailVO confirmedDetail(Integer id);
}
