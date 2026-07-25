package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.entity.GridCity;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.entity.GridProvince;
import com.neusoft.nep.entity.Supervisor;
import com.neusoft.nep.entity.Statistics;
import com.neusoft.nep.mapper.AqiFeedbackMapper;
import com.neusoft.nep.mapper.GridCityMapper;
import com.neusoft.nep.mapper.GridMemberMapper;
import com.neusoft.nep.mapper.GridProvinceMapper;
import com.neusoft.nep.mapper.SupervisorMapper;
import com.neusoft.nep.mapper.StatisticsMapper;
import com.neusoft.nep.service.AqiFeedbackService;
import com.neusoft.nep.vo.FeedbackDetailVO;
import com.neusoft.nep.vo.FeedbackListVO;
import com.neusoft.nep.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AqiFeedbackServiceImpl implements AqiFeedbackService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final AqiFeedbackMapper aqiFeedbackMapper;
    private final GridProvinceMapper gridProvinceMapper;
    private final GridCityMapper gridCityMapper;
    private final GridMemberMapper gridMemberMapper;
    private final SupervisorMapper supervisorMapper;
    private final StatisticsMapper statisticsMapper;

    public AqiFeedbackServiceImpl(AqiFeedbackMapper aqiFeedbackMapper,
                                  GridProvinceMapper gridProvinceMapper,
                                  GridCityMapper gridCityMapper,
                                  GridMemberMapper gridMemberMapper,
                                  SupervisorMapper supervisorMapper,
                                  StatisticsMapper statisticsMapper) {
        this.aqiFeedbackMapper = aqiFeedbackMapper;
        this.gridProvinceMapper = gridProvinceMapper;
        this.gridCityMapper = gridCityMapper;
        this.gridMemberMapper = gridMemberMapper;
        this.supervisorMapper = supervisorMapper;
        this.statisticsMapper = statisticsMapper;
    }

    @Override
    public void submit(FeedbackSubmitDTO dto) {
        if (!StringUtils.hasText(dto.getSupervisorId()) || dto.getProvinceId() == null
                || dto.getCityId() == null || dto.getEstimatedLevel() == null) {
            throw new BusinessException("必填参数不能为空");
        }
        if (!StringUtils.hasText(dto.getDetailAddress())) {
            throw new BusinessException("详细地址不能为空");
        }
        if (!StringUtils.hasText(dto.getFeedbackDesc())) {
            throw new BusinessException("反馈描述不能为空");
        }

        LocalDateTime now = LocalDateTime.now();
        AqiFeedback feedback = new AqiFeedback();
        feedback.setTelId(dto.getSupervisorId());
        feedback.setProvinceId(dto.getProvinceId());
        feedback.setCityId(dto.getCityId());
        feedback.setAddress(dto.getDetailAddress());
        feedback.setInformation(dto.getFeedbackDesc());
        feedback.setEstimatedGrade(dto.getEstimatedLevel());
        feedback.setAfDate(now.format(DATE_FMT));
        feedback.setAfTime(now.format(TIME_FMT));
        feedback.setGmId(0);
        feedback.setState(0);
        aqiFeedbackMapper.insert(feedback);
    }

    @Override
    public List<FeedbackListVO> myList(String telId) {
        if (!StringUtils.hasText(telId)) {
            throw new BusinessException("supervisorId 不能为空");
        }
        List<AqiFeedback> list = aqiFeedbackMapper.selectList(
                new LambdaQueryWrapper<AqiFeedback>()
                        .eq(AqiFeedback::getTelId, telId)
                        .orderByDesc(AqiFeedback::getAfId));

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getCityId, GridCity::getCityName, (a, b) -> a));

        List<FeedbackListVO> result = new ArrayList<>();
        for (AqiFeedback item : list) {
            FeedbackListVO vo = new FeedbackListVO();
            vo.setId(item.getAfId());
            vo.setProvinceName(provinceMap.get(item.getProvinceId()));
            vo.setCityName(cityMap.get(item.getCityId()));
            vo.setDetailAddress(item.getAddress());
            vo.setEstimatedLevel(item.getEstimatedGrade());
            vo.setFeedbackDesc(item.getInformation());
            vo.setFeedbackTime(item.getAfDate() + " " + item.getAfTime());
            vo.setStatus(mapState(item.getState()));
            result.add(vo);
        }
        return result;
    }

    @Override
    public PageVO<FeedbackListVO> pageQuery(Map<String, Object> params) {
        Integer current = toInt(params.get("page"), 1);
        Integer size = toInt(params.get("size"), 10);

        LambdaQueryWrapper<AqiFeedback> wrapper = new LambdaQueryWrapper<>();

        if (params.get("provinceId") != null) {
            wrapper.eq(AqiFeedback::getProvinceId, params.get("provinceId"));
        }
        if (params.get("cityId") != null) {
            wrapper.eq(AqiFeedback::getCityId, params.get("cityId"));
        }
        if (params.get("aqiLevel") != null) {
            wrapper.eq(AqiFeedback::getEstimatedGrade, params.get("aqiLevel"));
        }
        if (params.get("state") != null) {
            wrapper.eq(AqiFeedback::getState, params.get("state"));
        }
        if (params.get("afDate") != null) {
            wrapper.eq(AqiFeedback::getAfDate, params.get("afDate"));
        }

        wrapper.orderByDesc(AqiFeedback::getAfId);

        Page<AqiFeedback> page = aqiFeedbackMapper.selectPage(new Page<>(current, size), wrapper);

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getCityId, GridCity::getCityName, (a, b) -> a));
        Map<String, String> supervisorNameMap = supervisorMapper.selectList(null).stream()
                .collect(Collectors.toMap(Supervisor::getTelId, Supervisor::getRealName, (a, b) -> a));

        List<FeedbackListVO> records = new ArrayList<>();
        for (AqiFeedback item : page.getRecords()) {
            FeedbackListVO vo = new FeedbackListVO();
            vo.setId(item.getAfId());
            vo.setProvinceName(provinceMap.get(item.getProvinceId()));
            vo.setCityName(cityMap.get(item.getCityId()));
            vo.setDetailAddress(item.getAddress());
            vo.setEstimatedLevel(item.getEstimatedGrade());
            vo.setFeedbackDesc(item.getInformation());
            vo.setFeedbackTime(item.getAfDate() + " " + item.getAfTime());
            vo.setStatus(mapState(item.getState()));
            records.add(vo);
        }

        return new PageVO<>(page.getTotal(), current, size, records);
    }

    @Override
    public FeedbackDetailVO detail(Integer id) {
        AqiFeedback item = aqiFeedbackMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("反馈不存在");
        }

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getProvinceId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getCityId, GridCity::getCityName, (a, b) -> a));

        FeedbackDetailVO vo = new FeedbackDetailVO();
        vo.setId(item.getAfId());
        vo.setSupervisorId(item.getTelId());
        vo.setSupervisorName(supervisorMapper.selectById(item.getTelId()) != null ?
                supervisorMapper.selectById(item.getTelId()).getRealName() : "");
        vo.setProvinceId(item.getProvinceId());
        vo.setProvinceName(provinceMap.get(item.getProvinceId()));
        vo.setCityId(item.getCityId());
        vo.setCityName(cityMap.get(item.getCityId()));
        vo.setAddress(item.getAddress());
        vo.setEstimatedLevel(item.getEstimatedGrade());
        vo.setFeedbackDesc(item.getInformation());
        vo.setFeedbackTime(item.getAfDate() + " " + item.getAfTime());
        vo.setState(item.getState());
        vo.setStateText(mapState(item.getState()));
        vo.setGmId(item.getGmId());
        vo.setAssignTime(item.getAssignDate() != null ? item.getAssignDate() + " " + item.getAssignTime() : null);

        if (item.getGmId() != null && item.getGmId() > 0) {
            GridMember gm = gridMemberMapper.selectById(item.getGmId());
            if (gm != null) {
                vo.setGridMemberName(gm.getGmName());
            }
        }

        if (item.getState() == 2) {
            Statistics stat = statisticsMapper.selectOne(
                    new LambdaQueryWrapper<Statistics>()
                            .eq(Statistics::getFdId, item.getTelId())
                            .eq(Statistics::getInformation, item.getInformation())
                            .last("LIMIT 1")
            );
            if (stat != null) {
                vo.setSo2Value(stat.getSo2Value());
                vo.setSo2Level(stat.getSo2Level());
                vo.setCoValue(stat.getCoValue());
                vo.setCoLevel(stat.getCoLevel());
                vo.setSpmValue(stat.getSpmValue());
                vo.setSpmLevel(stat.getSpmLevel());
                vo.setAqiLevel(stat.getAqiId());
            }
        }

        return vo;
    }

    @Override
    @Transactional
    public void assign(Integer feedbackId, Integer gmId) {
        AqiFeedback feedback = aqiFeedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new BusinessException("反馈不存在");
        }
        if (feedback.getState() == 2) {
            throw new BusinessException("该反馈已确认，无法指派");
        }
        GridMember gm = gridMemberMapper.selectById(gmId);
        if (gm == null) {
            throw new BusinessException("网格员不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        feedback.setGmId(gmId);
        feedback.setAssignDate(now.format(DATE_FMT));
        feedback.setAssignTime(now.format(TIME_FMT));
        feedback.setState(1);
        aqiFeedbackMapper.updateById(feedback);
    }

    private String mapState(Integer state) {
        if (state == null) {
            return "未知";
        }
        return switch (state) {
            case 0 -> "未指派";
            case 1 -> "已指派";
            case 2 -> "已确认";
            default -> "未知";
        };
    }

    private static int toInt(Object val, int defaultVal) {
        if (val == null) return defaultVal;
        if (val instanceof Number n) return n.intValue();
        try { return Integer.parseInt(val.toString()); } catch (NumberFormatException e) { return defaultVal; }
    }
}
