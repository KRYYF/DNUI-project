package com.neusoft.nep.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简易 Token 工具：token -> 监督员手机号 telId
 */
public final class TokenUtil {

    private static final Map<String, String> TOKEN_MAP = new ConcurrentHashMap<>();

    private TokenUtil() {
    }

    public static String createToken(String telId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_MAP.put(token, telId);
        return token;
    }

    public static String getTelId(String token) {
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
