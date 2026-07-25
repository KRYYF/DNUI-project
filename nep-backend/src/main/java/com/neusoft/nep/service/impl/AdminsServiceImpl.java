package com.neusoft.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nep.common.BusinessException;
import com.neusoft.nep.entity.Admins;
import com.neusoft.nep.mapper.AdminsMapper;
import com.neusoft.nep.service.AdminsService;
import com.neusoft.nep.utils.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminsServiceImpl implements AdminsService {

    private final AdminsMapper adminsMapper;

    public AdminsServiceImpl(AdminsMapper adminsMapper) {
        this.adminsMapper = adminsMapper;
    }

    @Override
    public Map<String, Object> login(String adminCode, String password) {
        Admins admin = adminsMapper.selectOne(
                new LambdaQueryWrapper<Admins>()
                        .eq(Admins::getAdminCode, adminCode)
        );
        if (admin == null) {
            throw new BusinessException("账号不存在");
        }
        if (!password.equals(admin.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = TokenUtil.createToken("admin_" + admin.getAdminId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("adminId", admin.getAdminId());
        result.put("adminCode", admin.getAdminCode());
        return result;
    }
}
