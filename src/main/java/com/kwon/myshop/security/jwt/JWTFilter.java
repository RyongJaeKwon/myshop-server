package com.kwon.myshop.security.jwt;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        log.info("URI check : " + path);

        /**
         * /member/ 경로 호출은 체크 x
         */
        if (path.startsWith("/member/")) {
            return true;
        }

        if (path.startsWith("/items/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("--------------------JWT Filter--------------------");

        String authorizationHeader = request.getHeader("Authorization");


        try {
            String accessToken = authorizationHeader.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("jwt check error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String json = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN", "msg", e.getMessage()));

            response.setContentType("application/json; charset=UTF-8");

            response.getWriter().write(json);
        }
    }
}
