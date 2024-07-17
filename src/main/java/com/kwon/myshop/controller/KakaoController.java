package com.kwon.myshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kwon.myshop.dto.MemberDetails;
import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.dto.MemberUpdate;
import com.kwon.myshop.security.jwt.JWTUtil;
import com.kwon.myshop.service.KakaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/member/getkakao")
    public Map<String, Object> getMemberFromKakao(String accessToken) {
        log.info("accessToken: " + accessToken);

        return kakaoService.getKakaoMember(accessToken);
    }

    @PostMapping("/member/kakao/")
    public Map<String, Object> create(@Valid @RequestBody MemberUpdate memberUpdate) {

        return kakaoService.KakaoSignup(memberUpdate);
    }
}
