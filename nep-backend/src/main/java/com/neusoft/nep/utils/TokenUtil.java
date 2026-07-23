package com.neusoft.nep.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简易 Token 工具（内存存储，适合课程演示）
 */
public final class TokenUtil {

    private static final Map<String, Integer> TOKEN_MAP = new ConcurrentHashMap<>();

    private TokenUtil() {
    }

    public static String createToken(Integer supervisorId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_MAP.put(token, supervisorId);
        return token;
    }

    public static Integer getSupervisorId(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return TOKEN_MAP.get(token);
    }

    public static void removeToken(String token) {
        if (token != null) {
            TOKEN_MAP.remove(token);
        }
    }
}
