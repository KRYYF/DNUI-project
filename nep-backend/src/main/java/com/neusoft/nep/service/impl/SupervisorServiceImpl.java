package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.dto.SupervisorLoginDTO;
import com.neusoft.nep.dto.SupervisorRegisterDTO;
import com.neusoft.nep.entity.Supervisor;
import com.neusoft.nep.mapper.SupervisorMapper;
import com.neusoft.nep.service.SupervisorService;
import com.neusoft.nep.utils.MD5Util;
import com.neusoft.nep.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorMapper supervisorMapper;

    public SupervisorServiceImpl(SupervisorMapper supervisorMapper) {
        this.supervisorMapper = supervisorMapper;
    }

    @Override
    public void register(SupervisorRegisterDTO dto) {
        if (!StringUtils.hasText(dto.getPhone()) || !StringUtils.hasText(dto.getPassword())
                || !StringUtils.hasText(dto.getRealName())) {
            throw new BusinessException("手机号、密码、姓名不能为空");
        }
        Long count = supervisorMapper.selectCount(
                new LambdaQueryWrapper<Supervisor>().eq(Supervisor::getPhone, dto.getPhone()));
        if (count != null && count > 0) {
            throw new BusinessException("该手机号已注册");
        }

        Supervisor supervisor = new Supervisor();
        supervisor.setPhone(dto.getPhone());
        supervisor.setPassword(MD5Util.encrypt(dto.getPassword()));
        supervisor.setRealName(dto.getRealName());
        supervisor.setGender(dto.getGender());
        if (StringUtils.hasText(dto.getBirthDate())) {
            supervisor.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        }
        supervisor.setCreateTime(LocalDateTime.now());
        supervisorMapper.insert(supervisor);
    }

    @Override
    public Map<String, Object> checkPhone(String phone) {
        Long count = supervisorMapper.selectCount(
                new LambdaQueryWrapper<Supervisor>().eq(Supervisor::getPhone, phone));
        Map<String, Object> data = new HashMap<>();
        data.put("exists", count != null && count > 0);
        return data;
    }

    @Override
    public Map<String, Object> login(SupervisorLoginDTO dto) {
        if (!StringUtils.hasText(dto.getPhone()) || !StringUtils.hasText(dto.getPassword())) {
            throw new BusinessException("手机号和密码不能为空");
        }
        Supervisor supervisor = supervisorMapper.selectOne(
                new LambdaQueryWrapper<Supervisor>().eq(Supervisor::getPhone, dto.getPhone()));
        if (supervisor == null) {
            throw new BusinessException("账号不存在");
        }
        if (!MD5Util.check(dto.getPassword(), supervisor.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = TokenUtil.createToken(supervisor.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("supervisorId", supervisor.getId());
        data.put("realName", supervisor.getRealName());
        return data;
    }
}
