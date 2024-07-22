package com.kwon.myshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Role;
import com.kwon.myshop.dto.MemberDetails;
import com.kwon.myshop.dto.MemberUpdate;
import com.kwon.myshop.exception.MyshopJWTException;
import com.kwon.myshop.repository.MemberRepository;
import com.kwon.myshop.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public Map<String, Object> getKakaoMember(String accessToken) {
        try {
            Map<String, String> result = getEmailAndNicknameFromKakaoAccessToken(accessToken);
            String email = result.get("email");
            String nickname = result.get("nickname");

            Optional<Member> savedMember = memberRepository.findByUserId(email);

            if (savedMember.isPresent()) {
                MemberDetails memberDetails = entityToDto(savedMember.get());
                Map<String, Object> claims = memberDetails.getClaims();

                String jwtAccessToken = jwtUtil.createAccessToken(claims);
                String jwtRefreshToken = jwtUtil.createRefreshToken(claims);

                claims.put("accessToken", jwtAccessToken);
                claims.put("refreshToken", jwtRefreshToken);

                return claims;
            }

            Address tempAddress = Address.builder().postcode("").basic_address("").detail_address("").build();
            MemberDetails memberDetails = new MemberDetails(null, email, "", "", nickname, "", tempAddress, "MEMBER");
            Map<String, Object> claims = memberDetails.getClaims();

            return claims;
        } catch (JsonProcessingException e) {
            log.error("JSON processing error", e);
            throw new RuntimeException("JSON processing error", e);
        }

    }

    public Map<String, String> getEmailAndNicknameFromKakaoAccessToken(String accessToken) throws JsonProcessingException {
        if (accessToken == null) {
            throw new MyshopJWTException("AccessToken is NULL");
        }

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, Object> bodyMap = objectMapper.readValue(response.getBody(), LinkedHashMap.class);

        LinkedHashMap<String, Object> kakaoAccount = (LinkedHashMap<String, Object>) bodyMap.get("kakao_account");
        LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        Map<String, String> result = new HashMap<>();
        result.put("email", email);
        result.put("nickname", nickname);
        log.info("result: " + result);

        return result;
    }

    public Map<String, Object> KakaoSignup(MemberUpdate memberUpdate) {
        Member savedMember = memberRepository.save(DtoToEntity(memberUpdate));

        MemberDetails memberDetails = entityToDto(savedMember);
        Map<String, Object> claims = memberDetails.getClaims();

        String jwtAccessToken = jwtUtil.createAccessToken(claims);
        String jwtRefreshToken = jwtUtil.createRefreshToken(claims);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        log.info("kakao claims: " + claims);

        return claims;
    }

    public Member DtoToEntity(MemberUpdate memberUpdate) {
        return Member.builder()
                .userId(memberUpdate.getUserId())
                .name(memberUpdate.getName())
                .phone(memberUpdate.getPhone())
                .address(Address.builder()
                        .postcode(memberUpdate.getAddress().getPostcode())
                        .basic_address(memberUpdate.getAddress().getBasic_address())
                        .detail_address(memberUpdate.getAddress().getDetail_address())
                        .build())
                .role(Role.valueOf(memberUpdate.getRole()))
                .build();
    }

    public MemberDetails entityToDto(Member member) {
        MemberDetails memberDetails = new MemberDetails(
                member.getId(),
                member.getUserId(),
                member.getPassword(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                new Address(
                        member.getAddress().getPostcode(),
                        member.getAddress().getBasic_address(),
                        member.getAddress().getDetail_address()
                ),
                member.getRole().getValue()
        );

        return memberDetails;
    }
}
