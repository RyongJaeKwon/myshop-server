package com.kwon.myshop.security;

import com.google.gson.Gson;
import com.kwon.myshop.dto.MemberDetails;
import com.kwon.myshop.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("--------------------onAuthenticationSuccess----------------------");
        log.info(String.valueOf(authentication));
        log.info("--------------------onAuthenticationSuccess----------------------");

        /**
         * 인증된 사용자 정보에 accessToken, refreshToken을 추가해서 반환한다.
         */
        MemberDetails memberDetails = (MemberDetails)authentication.getPrincipal();

        Map<String, Object> claims = memberDetails.getClaims();
        String accessToken = jwtUtil.createAccessToken(claims);
        String refreshToken = jwtUtil.createRefreshToken(claims);
        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();
        String json = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());

        response.getWriter().write(json);
    }
}
