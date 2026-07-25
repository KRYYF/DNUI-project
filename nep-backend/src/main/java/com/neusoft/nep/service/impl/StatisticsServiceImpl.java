package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.entity.*;
import com.neusoft.nep.mapper.*;
import com.neusoft.nep.service.StatisticsService;
import com.neusoft.nep.vo.PageVO;
import com.neusoft.nep.vo.StatisticsDetailVO;
import com.neusoft.nep.vo.StatisticsListVO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final AqiFeedbackMapper aqiFeedbackMapper;
    private final GridProvinceMapper gridProvinceMapper;
    private final GridCityMapper gridCityMapper;
    private final GridMemberMapper gridMemberMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper,
                                  AqiFeedbackMapper aqiFeedbackMapper,
                                  GridProvinceMapper gridProvinceMapper,
                                  GridCityMapper gridCityMapper,
                                  GridMemberMapper gridMemberMapper) {
        this.statisticsMapper = statisticsMapper;
        this.aqiFeedbackMapper = aqiFeedbackMapper;
        this.gridProvinceMapper = gridProvinceMapper;
        this.gridCityMapper = gridCityMapper;
        this.gridMemberMapper = gridMemberMapper;
    }

    @Override
    public List<Map<String, Object>> getProvinceExceed() {
        List<Statistics> stats = statisticsMapper.selectList(null);
        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));

        // 按省分组统计各分项超标数量
        Map<Integer, Long> so2ExceedMap = stats.stream()
                .filter(s -> s.getSo2Level() != null && s.getSo2Level() > 2)
                .collect(Collectors.groupingBy(Statistics::getProvinceId, Collectors.counting()));
        Map<Integer, Long> coExceedMap = stats.stream()
                .filter(s -> s.getCoLevel() != null && s.getCoLevel() > 2)
                .collect(Collectors.groupingBy(Statistics::getProvinceId, Collectors.counting()));
        Map<Integer, Long> spmExceedMap = stats.stream()
                .filter(s -> s.getSpmLevel() != null && s.getSpmLevel() > 2)
                .collect(Collectors.groupingBy(Statistics::getProvinceId, Collectors.counting()));
        Map<Integer, Long> aqiExceedMap = stats.stream()
                .filter(s -> s.getAqiId() != null && s.getAqiId() > 2)
                .collect(Collectors.groupingBy(Statistics::getProvinceId, Collectors.counting()));

        // 收集所有出现过的省ID
        Set<Integer> allProvinceIds = new HashSet<>();
        allProvinceIds.addAll(so2ExceedMap.keySet());
        allProvinceIds.addAll(coExceedMap.keySet());
        allProvinceIds.addAll(spmExceedMap.keySet());
        allProvinceIds.addAll(aqiExceedMap.keySet());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer provinceId : allProvinceIds) {
            Map<String, Object> item = new HashMap<>();
            item.put("provinceId", provinceId);
            item.put("provinceName", provinceMap.getOrDefault(provinceId, "未知"));
            // 统一字段名：NEPM & NEPV 通用
            item.put("so2Exceed", so2ExceedMap.getOrDefault(provinceId, 0L));
            item.put("coExceed", coExceedMap.getOrDefault(provinceId, 0L));
            item.put("pm25Exceed", spmExceedMap.getOrDefault(provinceId, 0L));
            item.put("aqiExceed", aqiExceedMap.getOrDefault(provinceId, 0L));
            // 兼容旧字段（管理员前端过渡期使用）
            item.put("so2ExceedCount", so2ExceedMap.getOrDefault(provinceId, 0L));
            item.put("coExceedCount", coExceedMap.getOrDefault(provinceId, 0L));
            item.put("spmExceedCount", spmExceedMap.getOrDefault(provinceId, 0L));
            item.put("aqiExceedCount", aqiExceedMap.getOrDefault(provinceId, 0L));
            result.add(item);
        }
        result.sort((a, b) -> Long.compare((Long) b.get("aqiExceedCount"), (Long) a.get("aqiExceedCount")));
        return result;
    }

    @Override
    public List<Map<String, Object>> getAqiDistribution() {
        List<Statistics> stats = statisticsMapper.selectList(null);

        Map<Integer, Long> distribution = stats.stream()
                .filter(s -> s.getAqiId() != null)
                .collect(Collectors.groupingBy(Statistics::getAqiId, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        String[] grades = {"", "优", "良", "轻度污染", "中度污染", "重度污染", "严重污染"};
        for (int i = 1; i <= 6; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("level", i);
            item.put("grade", i < grades.length ? grades[i] : "未知");
            item.put("count", distribution.getOrDefault(i, 0L));
            // 统一字段名：NEPV 饼图使用 name/value
            item.put("name", i < grades.length ? grades[i] : "未知");
            item.put("value", distribution.getOrDefault(i, 0L));
            result.add(item);
        }
        return result;
    }

    @Override
    public Map<String, Object> getAqiTrend() {
        // 生成最近12个月的月份列表
        List<String> monthLabels = new ArrayList<>();
        java.time.LocalDate now = java.time.LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            java.time.LocalDate d = now.minusMonths(i);
            monthLabels.add(String.format("%d-%02d", d.getYear(), d.getMonthValue()));
        }

        // 从 statistics 表查询超标记录，按月分组
        List<Statistics> stats = statisticsMapper.selectList(null);
        Map<String, Long> monthlyCount = stats.stream()
                .filter(s -> s.getConfirmDate() != null && s.getAqiId() != null && s.getAqiId() > 2)
                .collect(Collectors.groupingBy(
                        s -> s.getConfirmDate().substring(0, 7),
                        Collectors.counting()
                ));

        List<Long> exceedCounts = new ArrayList<>();
        for (String month : monthLabels) {
            exceedCounts.add(monthlyCount.getOrDefault(month, 0L));
        }

        Map<String, Object> result = new HashMap<>();
        // 统一字段名：NEPV 使用 months / exceedCounts
        result.put("months", monthLabels);
        result.put("exceedCounts", exceedCounts);
        // 兼容旧字段
        result.put("labels", monthLabels);
        result.put("values", exceedCounts);
        return result;
    }

    @Override
    public Map<String, Object> getGridCoverage() {
        // 全国省份总数（34个省级行政区）
        final int CHINA_TOTAL_PROVINCES = 34;
        // 全国大城市总数（105个大城市）
        final int CHINA_TOTAL_MAJOR_CITIES = 105;

        // 查询所有在工作状态的网格员（state=0）
        List<GridMember> activeMembers = gridMemberMapper.selectList(
                new LambdaQueryWrapper<GridMember>()
                        .eq(GridMember::getState, 0)
        );

        // 统计部署了工作状态网格员的省份数（去重）
        long coveredProvinces = activeMembers.stream()
                .map(GridMember::getProvinceId)
                .distinct()
                .count();

        // 统计部署了工作状态网格员的城市数（去重）
        long coveredCities = activeMembers.stream()
                .map(GridMember::getCityId)
                .distinct()
                .count();

        double provincePercent = Math.round((double) coveredProvinces / CHINA_TOTAL_PROVINCES * 10000.0) / 100.0;
        double cityPercent = Math.round((double) coveredCities / CHINA_TOTAL_MAJOR_CITIES * 10000.0) / 100.0;

        Map<String, Object> result = new HashMap<>();
        result.put("provinceCoverage", provincePercent);
        result.put("provinceCovered", coveredProvinces);
        result.put("provinceTotal", CHINA_TOTAL_PROVINCES);
        result.put("cityCoverage", cityPercent);
        result.put("cityCovered", coveredCities);
        result.put("cityTotal", CHINA_TOTAL_MAJOR_CITIES);
        // 兼容旧字段
        result.put("coverage", provincePercent);
        result.put("total", CHINA_TOTAL_PROVINCES);
        result.put("assigned", coveredProvinces);
        return result;
    }

    @Override
    public Map<String, Object> getRealTimeCount() {
        // 需求规格说明书：统计 statistics 表中 AQI 检测累计数量/良好数量/超标数量
        List<Statistics> allStats = statisticsMapper.selectList(null);
        long totalCount = allStats.size();
        long goodCount = allStats.stream().filter(s -> s.getAqiId() != null && s.getAqiId() <= 2).count();
        long pollutionCount = allStats.stream().filter(s -> s.getAqiId() != null && s.getAqiId() > 2).count();

        Map<String, Object> result = new HashMap<>();
        // 统一字段名：NEPV 使用 totalCount / goodCount / pollutionCount
        result.put("totalCount", totalCount);
        result.put("goodCount", goodCount);
        result.put("pollutionCount", pollutionCount);
        // 兼容旧字段
        result.put("totalFeedback", totalCount);
        result.put("assigned", goodCount);
        result.put("confirmed", pollutionCount);
        return result;
    }

    @Override
    public PageVO<StatisticsListVO> confirmedPageQuery(Map<String, Object> params) {
        Integer current = toInt(params.get("page"), 1);
        Integer size = toInt(params.get("size"), 10);

        LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();

        if (params.get("provinceId") != null) {
            wrapper.eq(Statistics::getProvinceId, params.get("provinceId"));
        }
        if (params.get("cityId") != null) {
            wrapper.eq(Statistics::getCityId, params.get("cityId"));
        }
        if (params.get("confirmDate") != null) {
            wrapper.eq(Statistics::getConfirmDate, params.get("confirmDate"));
        }

        wrapper.orderByDesc(Statistics::getId);

        Page<Statistics> page = statisticsMapper.selectPage(new Page<>(current, size), wrapper);

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getCityId, GridCity::getCityName, (a, b) -> a));
        Map<Integer, String> gmNameMap = gridMemberMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridMember::getGmId, GridMember::getGmName, (a, b) -> a));

        List<StatisticsListVO> records = new ArrayList<>();
        for (Statistics item : page.getRecords()) {
            StatisticsListVO vo = new StatisticsListVO();
            vo.setId(item.getId());
            vo.setProvinceId(item.getProvinceId());
            vo.setProvinceName(provinceMap.get(item.getProvinceId()));
            vo.setCityId(item.getCityId());
            vo.setCityName(cityMap.get(item.getCityId()));
            vo.setAddress(item.getAddress());
            vo.setSo2Value(item.getSo2Value());
            vo.setSo2Level(item.getSo2Level());
            vo.setCoValue(item.getCoValue());
            vo.setCoLevel(item.getCoLevel());
            vo.setSpmValue(item.getSpmValue());
            vo.setSpmLevel(item.getSpmLevel());
            vo.setAqiLevel(item.getAqiId());
            vo.setConfirmDate(item.getConfirmDate());
            vo.setConfirmTime(item.getConfirmTime());
            vo.setGmId(item.getGmId());
            vo.setGridMemberName(gmNameMap.get(item.getGmId()));
            records.add(vo);
        }

        return new PageVO<>(page.getTotal(), current, size, records);
    }

    @Override
    public StatisticsDetailVO confirmedDetail(Integer id) {
        Statistics item = statisticsMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("确认AQI数据不存在");
        }

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getCityId, GridCity::getCityName, (a, b) -> a));

        StatisticsDetailVO vo = new StatisticsDetailVO();
        vo.setId(item.getId());
        vo.setProvinceId(item.getProvinceId());
        vo.setProvinceName(provinceMap.get(item.getProvinceId()));
        vo.setCityId(item.getCityId());
        vo.setCityName(cityMap.get(item.getCityId()));
        vo.setAddress(item.getAddress());
        vo.setSo2Value(item.getSo2Value());
        vo.setSo2Level(item.getSo2Level());
        vo.setCoValue(item.getCoValue());
        vo.setCoLevel(item.getCoLevel());
        vo.setSpmValue(item.getSpmValue());
        vo.setSpmLevel(item.getSpmLevel());
        vo.setAqiLevel(item.getAqiId());
        vo.setConfirmDate(item.getConfirmDate());
        vo.setConfirmTime(item.getConfirmTime());
        vo.setGmId(item.getGmId());
        vo.setFdId(item.getFdId());
        vo.setInformation(item.getInformation());
        vo.setRemarks(item.getRemarks());

        if (item.getGmId() != null && item.getGmId() > 0) {
            GridMember gm = gridMemberMapper.selectById(item.getGmId());
            if (gm != null) {
                vo.setGridMemberName(gm.getGmName());
            }
        }

        return vo;
    }

    private static int toInt(Object val, int defaultVal) {
        if (val == null) return defaultVal;
        if (val instanceof Number n) return n.intValue();
        try { return Integer.parseInt(val.toString()); } catch (NumberFormatException e) { return defaultVal; }
    }
}
