package com.kwon.myshop.security.jwt;

import com.kwon.myshop.exception.MyshopJWTException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    private static SecretKey secretKey;

//    @Value("${spring.jwt.secretkey}")
    private String key = "rnjsdfklasnme213njsfasljzx09sdfknewqmsadfk";

    @PostConstruct
    public void init() {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is not configured properly.");
        }
        secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public static String createJwt(Map<String, Object> valueMap, int min) {

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new MyshopJWTException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new MyshopJWTException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new MyshopJWTException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new MyshopJWTException("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            throw new MyshopJWTException("Error");
        }

        return claims;
    }
}
