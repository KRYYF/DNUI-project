package com.neusoft.nep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nep.entity.Aqi;
import org.apache.ibatis.annotations.Param;

public interface AqiMapper extends BaseMapper<Aqi> {
    /**
     * 根据三项污染物数值匹配对应的AQI等级
     */
    Aqi matchAqiLevel(@Param("so2") Integer so2,
                      @Param("co") Integer co,
                      @Param("spm") Integer spm);
}
