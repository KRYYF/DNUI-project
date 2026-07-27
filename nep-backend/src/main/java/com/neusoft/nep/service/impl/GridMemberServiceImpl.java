package com.neusoft.nep.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.dto.GridMemberAqiSubmitDTO;
import com.neusoft.nep.dto.GridMemberLoginDTO;
import com.neusoft.nep.entity.Aqi;
import com.neusoft.nep.entity.AqiFeedback;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.entity.Statistics;
import com.neusoft.nep.mapper.AqiFeedbackMapper;
import com.neusoft.nep.mapper.AqiMapper;
import com.neusoft.nep.mapper.GridMemberMapper;
import com.neusoft.nep.mapper.StatisticsMapper;
import com.neusoft.nep.service.GridMemberService;
import com.neusoft.nep.utils.TokenUtil;
import com.neusoft.nep.vo.GridFeedbackDetailVO;
import com.neusoft.nep.vo.GridFeedbackVO;
import com.neusoft.nep.vo.GridMemberLoginVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GridMemberServiceImpl implements GridMemberService {


    private final GridMemberMapper gridMemberMapper;

    private final AqiFeedbackMapper aqiFeedbackMapper;

    private final AqiMapper aqiMapper;

    private final StatisticsMapper statisticsMapper;


    public GridMemberServiceImpl(
            GridMemberMapper gridMemberMapper,
            AqiFeedbackMapper aqiFeedbackMapper,
            AqiMapper aqiMapper,
            StatisticsMapper statisticsMapper
    ) {
        this.gridMemberMapper = gridMemberMapper;
        this.aqiFeedbackMapper = aqiFeedbackMapper;
        this.aqiMapper = aqiMapper;
        this.statisticsMapper = statisticsMapper;
    }



    /**
     * PR1原有功能
     */
    @Override
    public List<GridMember> getByRegion(Integer provinceId, Integer cityId) {

        LambdaQueryWrapper<GridMember> wrapper =
                new LambdaQueryWrapper<>();

        if (provinceId != null) {
            wrapper.eq(
                    GridMember::getProvinceId,
                    provinceId
            );
        }

        if (cityId != null) {
            wrapper.eq(
                    GridMember::getCityId,
                    cityId
            );
        }

        wrapper.eq(
                GridMember::getState,
                0
        );


        return gridMemberMapper.selectList(wrapper);
    }



    @Override
    public List<GridMember> getAll() {

        return gridMemberMapper.selectList(null);

    }



    @Override
    public GridMember getById(Integer gmId) {

        return gridMemberMapper.selectById(gmId);

    }





    /**
     * 网格员登录
     */
    @Override
    public GridMemberLoginVO login(GridMemberLoginDTO dto) {


        GridMember gm =
                gridMemberMapper.selectOne(
                        new LambdaQueryWrapper<GridMember>()
                                .eq(
                                        GridMember::getGmCode,
                                        dto.getGmCode()
                                )
                );


        if (gm == null) {

            throw new BusinessException(
                    "账号不存在"
            );

        }


        if (!gm.getPassword()
                .equals(dto.getPassword())) {


            throw new BusinessException(
                    "密码错误"
            );

        }


        String token =
                TokenUtil.createToken(
                        "gm_" + gm.getGmId()
                );



        GridMemberLoginVO vo =
                new GridMemberLoginVO();


        vo.setToken(token);
        vo.setGmId(gm.getGmId());
        vo.setGmName(gm.getGmName());
        vo.setGmCode(gm.getGmCode());


        return vo;

    }





    /**
     * 查询任务列表
     */
    @Override
    public Page<GridFeedbackVO> pageTasks(
            String token,
            Integer current,
            Integer size) {


        Integer gmId =
                getCurrentGmId(token);



        Page<AqiFeedback> page =
                new Page<>(
                        current,
                        size
                );



        LambdaQueryWrapper<AqiFeedback> wrapper =
                new LambdaQueryWrapper<>();


        wrapper.eq(
                AqiFeedback::getGmId,
                gmId
        );


        Page<AqiFeedback> result =
                aqiFeedbackMapper.selectPage(
                        page,
                        wrapper
                );



        Page<GridFeedbackVO> voPage =
                new Page<>();


        voPage.setCurrent(
                result.getCurrent()
        );

        voPage.setSize(
                result.getSize()
        );

        voPage.setTotal(
                result.getTotal()
        );



        List<GridFeedbackVO> list =
                result.getRecords()
                        .stream()
                        .map(this::convertVO)
                        .collect(Collectors.toList());


        voPage.setRecords(list);


        return voPage;

    }





    /**
     * 任务详情
     */
    @Override
    public GridFeedbackDetailVO detail(
            String token,
            Integer afId) {


        Integer gmId =
                getCurrentGmId(token);



        AqiFeedback feedback =
                aqiFeedbackMapper.selectById(
                        afId
                );


        if (feedback == null) {

            throw new BusinessException(
                    "任务不存在"
            );

        }



        if (!feedback.getGmId()
                .equals(gmId)) {


            throw new BusinessException(
                    "无权查看该任务"
            );

        }



        GridFeedbackDetailVO vo =
                new GridFeedbackDetailVO();


        vo.setAfId(
                feedback.getAfId()
        );

        vo.setTelId(
                feedback.getTelId()
        );

        vo.setAddress(
                feedback.getAddress()
        );

        vo.setInformation(
                feedback.getInformation()
        );

        vo.setEstimatedGrade(
                feedback.getEstimatedGrade()
        );

        vo.setState(
                feedback.getState()
        );


        return vo;

    }





    /**
     * AQI反馈提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAqi(
            String token,
            GridMemberAqiSubmitDTO dto) {


        Integer gmId =
                getCurrentGmId(token);



        AqiFeedback feedback =
                aqiFeedbackMapper.selectById(
                        dto.getAfId()
                );



        if (feedback == null) {

            throw new BusinessException(
                    "任务不存在"
            );

        }



        if (!feedback.getGmId()
                .equals(gmId)) {

            throw new BusinessException(
                    "无权提交该任务"
            );

        }



        if (feedback.getState() == 2) {

            throw new BusinessException(
                    "任务已经完成"
            );

        }



        Integer so2 =
                dto.getSo2Value();

        Integer co =
                dto.getCoValue();

        Integer spm =
                dto.getSpmValue();



        Integer so2Level =
                calculateLevel(
                        so2,
                        1
                );


        Integer coLevel =
                calculateLevel(
                        co,
                        2
                );


        Integer spmLevel =
                calculateLevel(
                        spm,
                        3
                );



        feedback.setState(2);


        aqiFeedbackMapper.updateById(
                feedback
        );



        Statistics statistics =
                new Statistics();


        statistics.setProvinceId(
                feedback.getProvinceId()
        );

        statistics.setCityId(
                feedback.getCityId()
        );


        statistics.setAddress(
                feedback.getAddress()
        );


        statistics.setSo2Value(so2);
        statistics.setSo2Level(so2Level);


        statistics.setCoValue(co);
        statistics.setCoLevel(coLevel);


        statistics.setSpmValue(spm);
        statistics.setSpmLevel(spmLevel);


// 设置最终AQI等级
        statistics.setAqiId(
                Math.max(
                        so2Level,
                        Math.max(coLevel, spmLevel)
                )
        );


        statistics.setGmId(
                gmId
        );


        // 注意：PR审核要求
        // fdId = task.telId
        statistics.setFdId(
                feedback.getTelId()
        );



        statisticsMapper.insert(
                statistics
        );

    }





    private Integer calculateLevel(
            Integer value,
            Integer type) {


        List<Aqi> list =
                aqiMapper.selectList(null);


        for (Aqi aqi : list) {


            if(type == 1
                    && value >= aqi.getSo2Min()
                    && value <= aqi.getSo2Max()) {

                return aqi.getAqiId();

            }


            if(type == 2
                    && value >= aqi.getCoMin()
                    && value <= aqi.getCoMax()) {

                return aqi.getAqiId();

            }


            if(type == 3
                    && value >= aqi.getSpmMin()
                    && value <= aqi.getSpmMax()) {

                return aqi.getAqiId();

            }

        }


        return 6;

    }





    private Integer getCurrentGmId(String token) {


        String id =
                TokenUtil.getTelId(token);



        if (id == null) {

            throw new BusinessException(
                    "登录失效"
            );

        }


        return Integer.parseInt(
                id.replace("gm_", "")
        );

    }





    private GridFeedbackVO convertVO(
            AqiFeedback feedback) {


        GridFeedbackVO vo =
                new GridFeedbackVO();


        vo.setAfId(
                feedback.getAfId()
        );

        vo.setTelId(
                feedback.getTelId()
        );

        vo.setAddress(
                feedback.getAddress()
        );

        vo.setInformation(
                feedback.getInformation()
        );

        vo.setEstimatedGrade(
                feedback.getEstimatedGrade()
        );

        vo.setState(
                feedback.getState()
        );


        return vo;

    }

}