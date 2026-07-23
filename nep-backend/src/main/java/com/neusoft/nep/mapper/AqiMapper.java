package com.neusoft.nep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nep.entity.Aqi;
import org.apache.ibatis.annotations.Mapper;

/**
 * AQI 等级表 Mapper
 */
@Mapper
public interface AqiMapper extends BaseMapper<Aqi> {
}
