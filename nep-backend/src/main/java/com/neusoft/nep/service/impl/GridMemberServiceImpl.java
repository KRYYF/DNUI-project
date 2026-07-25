package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.mapper.GridMemberMapper;
import com.neusoft.nep.service.GridMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridMemberServiceImpl implements GridMemberService {

    private final GridMemberMapper gridMemberMapper;

    public GridMemberServiceImpl(GridMemberMapper gridMemberMapper) {
        this.gridMemberMapper = gridMemberMapper;
    }

    @Override
    public List<GridMember> getByRegion(Integer provinceId, Integer cityId) {
        LambdaQueryWrapper<GridMember> wrapper = new LambdaQueryWrapper<>();
        if (provinceId != null) {
            wrapper.eq(GridMember::getProvinceId, provinceId);
        }
        if (cityId != null) {
            wrapper.eq(GridMember::getCityId, cityId);
        }
        wrapper.eq(GridMember::getState, 0);
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
}
