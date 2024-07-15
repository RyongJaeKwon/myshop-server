package com.kwon.myshop.controller;

import com.kwon.myshop.exception.MyshopJWTException;
import com.kwon.myshop.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshController {

    private final JWTUtil jwtUtil;

    @PostMapping("/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
                                       @RequestHeader("X-Refresh-Token") String refreshHeader) {

        if (refreshHeader == null) {
            throw new MyshopJWTException("REFRESHTOKEN_NOT_FOUND");
        }
        if (authHeader == null || authHeader.length() < 7) {
            throw new MyshopJWTException("INVALID_ACCESSTOKEN");
        }

        String accessToken = authHeader.substring(7);
        String refreshToken = refreshHeader.substring(7);

        if (!jwtUtil.isAccessTokenExpired(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        return jwtUtil.isRefreshTokenExpired(refreshToken);
    }

}
