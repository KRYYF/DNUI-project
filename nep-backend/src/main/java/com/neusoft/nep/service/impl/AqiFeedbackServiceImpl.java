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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AqiFeedbackServiceImpl implements AqiFeedbackService {

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
        if (dto.getSupervisorId() == null || dto.getProvinceId() == null
                || dto.getCityId() == null || dto.getEstimatedLevel() == null) {
            throw new BusinessException("必填参数不能为空");
        }
        AqiFeedback feedback = new AqiFeedback();
        feedback.setSupervisorId(dto.getSupervisorId());
        feedback.setProvinceId(dto.getProvinceId());
        feedback.setCityId(dto.getCityId());
        feedback.setDetailAddress(dto.getDetailAddress());
        feedback.setEstimatedLevel(dto.getEstimatedLevel());
        feedback.setFeedbackDesc(dto.getFeedbackDesc());
        feedback.setFeedbackTime(LocalDateTime.now());
        feedback.setStatus("未指派");
        aqiFeedbackMapper.insert(feedback);
    }

    @Override
    public List<FeedbackListVO> myList(Integer supervisorId) {
        if (supervisorId == null) {
            throw new BusinessException("supervisorId 不能为空");
        }
        List<AqiFeedback> list = aqiFeedbackMapper.selectList(
                new LambdaQueryWrapper<AqiFeedback>()
                        .eq(AqiFeedback::getSupervisorId, supervisorId)
                        .orderByDesc(AqiFeedback::getFeedbackTime));

        Map<Integer, String> provinceMap = gridProvinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridProvince::getId, GridProvince::getProvinceName, (a, b) -> a));
        Map<Integer, String> cityMap = gridCityMapper.selectList(null).stream()
                .collect(Collectors.toMap(GridCity::getId, GridCity::getCityName, (a, b) -> a));

        List<FeedbackListVO> result = new ArrayList<>();
        for (AqiFeedback item : list) {
            FeedbackListVO vo = new FeedbackListVO();
            vo.setId(item.getId());
            vo.setProvinceName(provinceMap.get(item.getProvinceId()));
            vo.setCityName(cityMap.get(item.getCityId()));
            vo.setDetailAddress(item.getDetailAddress());
            vo.setEstimatedLevel(item.getEstimatedLevel());
            vo.setFeedbackDesc(item.getFeedbackDesc());
            vo.setFeedbackTime(item.getFeedbackTime());
            vo.setStatus(item.getStatus());
            result.add(vo);
        }
        return result;
    }
}
