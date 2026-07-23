package com.neusoft.nep.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具：MD5 + 盐 nep_2026_
 */
public final class MD5Util {

    private static final String SALT = "nep_2026_";

    private MD5Util() {
    }

    /**
     * 对明文密码加盐后 MD5 加密
     *
     * @param password 明文密码
     * @return 32 位小写十六进制摘要
     */
    public static String encrypt(String password) {
        return md5(SALT + password);
    }

    /**
     * 校验明文密码与密文是否匹配
     *
     * @param raw       明文密码
     * @param encrypted 已加密密文
     * @return 是否一致
     */
    public static boolean check(String raw, String encrypted) {
        if (raw == null || encrypted == null) {
            return false;
        }
        return encrypt(raw).equalsIgnoreCase(encrypted);
    }

    private static String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(32);
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 算法不可用", e);
        }
    }
}
