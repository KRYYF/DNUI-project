package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.dto.FeedbackSubmitDTO;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.entity.GridCity;
import com.neusoft.nep.entity.GridProvince;
import com.neusoft.nep.mapper.AqiFeedbackMapper;
import com.neusoft.nep.mapper.GridCityMapper;
import com.neusoft.nep.mapper.GridProvinceMapper;
import com.neusoft.nep.service.AqiFeedbackService;
import com.neusoft.nep.vo.FeedbackListVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AqiFeedbackServiceImpl implements AqiFeedbackService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final AqiFeedbackMapper aqiFeedbackMapper;
    private final GridProvinceMapper gridProvinceMapper;
    private final GridCityMapper gridCityMapper;

    public AqiFeedbackServiceImpl(AqiFeedbackMapper aqiFeedbackMapper,
                                  GridProvinceMapper gridProvinceMapper,
                                  GridCityMapper gridCityMapper) {
        this.aqiFeedbackMapper = aqiFeedbackMapper;
        this.gridProvinceMapper = gridProvinceMapper;
        this.gridCityMapper = gridCityMapper;
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
}
