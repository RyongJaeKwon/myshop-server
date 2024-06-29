package com.kwon.myshop.security;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class CustomLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("--------------------onAuthenticationFail----------------------");
        log.info(String.valueOf(exception));
        log.info("--------------------onAuthenticationFail----------------------");

        Gson gson = new Gson();
        String json = gson.toJson(Map.of("error", "ERROR_LOGIN", "msg", exception.getMessage()));

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(json);
    }
}
