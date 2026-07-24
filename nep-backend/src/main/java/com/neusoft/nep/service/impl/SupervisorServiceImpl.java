package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.dto.SupervisorLoginDTO;
import com.neusoft.nep.dto.SupervisorRegisterDTO;
import com.neusoft.nep.entity.Supervisor;
import com.neusoft.nep.mapper.SupervisorMapper;
import com.neusoft.nep.service.SupervisorService;
import com.neusoft.nep.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        if (dto.getPhone().length() > 11) {
            throw new BusinessException("手机号长度不能超过11位");
        }
        if (dto.getPassword().length() > 20) {
            throw new BusinessException("密码长度不能超过20位");
        }
        Supervisor exists = supervisorMapper.selectById(dto.getPhone());
        if (exists != null) {
            throw new BusinessException("该手机号已注册");
        }

        Supervisor supervisor = new Supervisor();
        supervisor.setTelId(dto.getPhone());
        // 官方库 password 为 varchar(20) 明文，与 dump 一致
        supervisor.setPassword(dto.getPassword());
        supervisor.setRealName(dto.getRealName());
        supervisor.setBirthday(StringUtils.hasText(dto.getBirthDate()) ? dto.getBirthDate() : "2000-01-01");
        supervisor.setSex("女".equals(dto.getGender()) ? 0 : 1);
        supervisorMapper.insert(supervisor);
    }

    @Override
    public Map<String, Object> checkPhone(String phone) {
        Supervisor exists = supervisorMapper.selectById(phone);
        Map<String, Object> data = new HashMap<>();
        data.put("exists", exists != null);
        return data;
    }

    @Override
    public Map<String, Object> login(SupervisorLoginDTO dto) {
        if (!StringUtils.hasText(dto.getPhone()) || !StringUtils.hasText(dto.getPassword())) {
            throw new BusinessException("手机号和密码不能为空");
        }
        Supervisor supervisor = supervisorMapper.selectById(dto.getPhone());
        if (supervisor == null) {
            throw new BusinessException("账号不存在");
        }
        if (!dto.getPassword().equals(supervisor.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = TokenUtil.createToken(supervisor.getTelId());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        // 兼容前端字段 supervisorId：官方库主键为手机号
        data.put("supervisorId", supervisor.getTelId());
        data.put("realName", supervisor.getRealName());
        return data;
    }
}
