package com.kwon.myshop.security.jwt;

import com.google.gson.Gson;
import com.kwon.myshop.domain.Address;
import com.kwon.myshop.dto.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

        log.info("URI check : " + request.getRequestURI());

        if (request.getRequestURI().startsWith("/member/")) {
            return true;
        }

        if (request.getMethod().equals(HttpMethod.GET.name()) && request.getRequestURI().equals("/items/")) {
            return true;
        }

        if (request.getRequestURI().startsWith("/items/view/")) {
            return true;
        }

        if (request.getRequestURI().matches("^/items/[^/]+/list$")) {
            return true;
        }

        if (request.getRequestURI().matches("/items/search")) {
            return true;
        }

        if (request.getRequestURI().matches("/replies/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("--------JWT Filter--------");

        String authorizationHeader = request.getHeader("Authorization");


        try {
            String accessToken = authorizationHeader.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            Long id = ((Number) claims.get("id")).longValue();
            String userId = (String) claims.get("userId");
            String email = (String) claims.get("email");
            String name = (String) claims.get("name");
            String phone = (String) claims.get("phone");
            String role = (String) claims.get("role");

            Map<String, Object> addressMap = (Map<String, Object>) claims.get("address");

            String postcode = (String) addressMap.get("postcode");
            String basicAddress = (String) addressMap.get("basic_address");
            String detailAddress = (String) addressMap.get("detail_address");
            Address address = Address.builder()
                    .postcode(postcode)
                    .basic_address(basicAddress)
                    .detail_address(detailAddress)
                    .build();

            MemberDetails memberDetails = new MemberDetails(id, userId, "", email, name, phone, address, role);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("--------jwt check error--------");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String json = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write(json);
        }
    }
}
