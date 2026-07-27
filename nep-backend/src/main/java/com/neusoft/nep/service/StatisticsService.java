package com.neusoft.nep.service;

import com.neusoft.nep.vo.PageVO;
import com.neusoft.nep.vo.StatisticsDetailVO;
import com.neusoft.nep.vo.StatisticsListVO;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口（PR#1：保留完整方法集，PR#2 在此基础上扩展）
 */
public interface StatisticsService {

    List<Map<String, Object>> getProvinceExceed();

    List<Map<String, Object>> getAqiDistribution();

    Map<String, Object> getAqiTrend();

    Map<String, Object> getGridCoverage();

    Map<String, Object> getRealTimeCount();

    PageVO<StatisticsListVO> confirmedPageQuery(Map<String, Object> params);

    StatisticsDetailVO confirmedDetail(Integer id);
}
