package com.neusoft.nep.config;

import com.neusoft.nep.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：注册鉴权拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/test/**",
                        "/api/supervisor/register",
                        "/api/supervisor/checkPhone",
                        "/api/supervisor/login",
                        "/api/admin/login",
                        // 大屏只读接口放行（PR#1 review）
                        "/api/statistics/provinceExceed",
                        "/api/statistics/aqiDistribution",
                        "/api/statistics/aqiTrend",
                        "/api/statistics/realTimeCount",
                        "/api/statistics/gridCoverage"
                );
    }
}
