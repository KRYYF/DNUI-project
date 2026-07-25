package com.neusoft.nep.service;

import com.neusoft.nep.entity.GridMember;

import java.util.List;

public interface GridMemberService {

    List<GridMember> getByRegion(Integer provinceId, Integer cityId);

    List<GridMember> getAll();

    GridMember getById(Integer gmId);
}
