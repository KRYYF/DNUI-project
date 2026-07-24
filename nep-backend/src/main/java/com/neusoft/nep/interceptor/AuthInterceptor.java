package com.neusoft.nep.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nep.common.R;
import com.neusoft.nep.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Token 校验拦截器（token 对应监督员手机号）
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        String telId = TokenUtil.getTelId(token);
        if (telId == null) {
            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(R.error(401, "未授权，请先登录")));
            return false;
        }
        request.setAttribute("telId", telId);
        return true;
    }
}
