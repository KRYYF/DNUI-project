package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.nep.entity.Statistics;
import com.neusoft.nep.mapper.StatisticsMapper;
import com.neusoft.nep.service.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {
}
