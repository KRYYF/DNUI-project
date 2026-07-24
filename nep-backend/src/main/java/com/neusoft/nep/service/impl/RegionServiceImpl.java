package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.entity.GridCity;
import com.neusoft.nep.entity.GridProvince;
import com.neusoft.nep.mapper.GridCityMapper;
import com.neusoft.nep.mapper.GridProvinceMapper;
import com.neusoft.nep.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    private final GridProvinceMapper gridProvinceMapper;
    private final GridCityMapper gridCityMapper;

    public RegionServiceImpl(GridProvinceMapper gridProvinceMapper, GridCityMapper gridCityMapper) {
        this.gridProvinceMapper = gridProvinceMapper;
        this.gridCityMapper = gridCityMapper;
    }

    @Override
    public List<GridProvince> listProvinces() {
        return gridProvinceMapper.selectList(
                new LambdaQueryWrapper<GridProvince>().orderByAsc(GridProvince::getProvinceId));
    }

    @Override
    public List<GridCity> listCitiesByProvinceId(Integer provinceId) {
        return gridCityMapper.selectList(
                new LambdaQueryWrapper<GridCity>()
                        .eq(GridCity::getProvinceId, provinceId)
                        .orderByAsc(GridCity::getCityId));
    }
}
