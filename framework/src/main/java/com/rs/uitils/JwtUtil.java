package com.rs.uitils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L; // 有效期24小时
    public static final String JWT_KEY = "MySuperSecureSecretKey1234567890abcdef"; // 32+字符

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成JWT
     */
    public static String createJWT(String subject) {
        return getJwtBuilder(subject, null, getUUID()).compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + (ttlMillis == null ? JWT_TTL : ttlMillis);
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("sg")
                .setIssuedAt(now)
                .signWith(generalKey(), SignatureAlgorithm.HS256) // 使用正确的密钥和算法
                .setExpiration(expDate);
    }

    /**
     * 生成HMAC-SHA256密钥
     */
    public static SecretKey generalKey() {
        byte[] keyBytes = JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 解析JWT
     */
    public static Claims parseJWT(String jwt) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(generalKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}