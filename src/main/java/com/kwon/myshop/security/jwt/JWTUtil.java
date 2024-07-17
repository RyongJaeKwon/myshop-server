package com.kwon.myshop.security.jwt;

import com.kwon.myshop.exception.MyshopJWTException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JWTUtil {

    private final SecretKey secretKey;
    private final int accessTokenExpirationMinutes;
    private final int refreshTokenExpirationMinutes;

    public JWTUtil(@Value("${spring.jwt.secret-key}") String key,
                   @Value("${spring.jwt.access-token-expiration-minutes}") int accessTokenExpirationMinutes,
                   @Value("${spring.jwt.refresh-token-expiration-minutes}") int refreshTokenExpirationMinutes) {
        if (key == null || key.isEmpty()) {
            throw new MyshopJWTException("비밀키를 찾을 수 없습니다");
        }
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    public String createAccessToken(Map<String, Object> valueMap) {
        return createJwt(valueMap, accessTokenExpirationMinutes);
    }

    public String createRefreshToken(Map<String, Object> valueMap) {
        return createJwt(valueMap, refreshTokenExpirationMinutes);
    }

    public String createJwt(Map<String, Object> valueMap, int min) {

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> validateToken(String token) {

        Map<String, Object> claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new MyshopJWTException("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            throw new MyshopJWTException("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            throw new MyshopJWTException("지원하지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            throw new MyshopJWTException("JWT 토큰이 잘못되었습니다");
        } catch (Exception e) {
            throw new MyshopJWTException("Error");
        }

        return claims;
    }

    public boolean isAccessTokenExpired(String accessToken) {

        try {
            validateToken(accessToken);
            return false;
        } catch (MyshopJWTException e) {
            if (e.getMessage().equals("만료된 JWT 토큰입니다")) {
                return true;
            } else {
                throw e;
            }
        }
    }

    public Map<String, Object> isRefreshTokenExpired(String refreshToken) {

        Map<String, Object> claims = validateToken(refreshToken);
        String newAccessToken = createAccessToken(claims);
        Object expObject = claims.get("exp");

        if (expObject instanceof Integer) {
            Integer expValue = (Integer) expObject;
            Date expiration = new Date(expValue * 1000L);
            Date now = new Date();

            if (expiration.getTime() - now.getTime() <= 10 * 60 * 1000) {
                String newRefreshToken = createRefreshToken(claims);
                return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
            }
        }

        return Map.of("accessToken", newAccessToken, "refreshToken", refreshToken);
    }

}
