package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.entity.Aqi;
import com.neusoft.nep.mapper.AqiMapper;
import com.neusoft.nep.service.AqiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AqiServiceImpl implements AqiService {

    private final AqiMapper aqiMapper;

    public AqiServiceImpl(AqiMapper aqiMapper) {
        this.aqiMapper = aqiMapper;
    }

    @Override
    public List<Aqi> listLevels() {
        return aqiMapper.selectList(new LambdaQueryWrapper<Aqi>().orderByAsc(Aqi::getAqiId));
    }
}
