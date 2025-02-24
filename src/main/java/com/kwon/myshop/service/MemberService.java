package com.kwon.myshop.service;

import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Role;
import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.dto.MemberResponse;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(MemberDto memberDto) {

        signup(memberDto);
    }

    public void update(MemberDto memberDto) {

        edit(memberDto);
    }

    public MemberResponse get(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new MemberNotFoundException());

        return MemberResponse.entityToDto(member);
    }

    public Map<String, String> duplicateUserIdCheck(String userId) {
        String userIdPattern = "^[a-zA-Z0-9]{5,12}$";
        Pattern pattern = Pattern.compile(userIdPattern);
        Matcher matcher = pattern.matcher(userId);

        if (!matcher.matches()) {
            Map<String, String> map = Collections.singletonMap("message", "올바르지 않는 형식입니다");
            return map;
        }

        return DuplicateUserIdCheck.getMessage(memberRepository.existsByUserId(userId));
    }

    public Map<String, String> duplicateEmailCheck(String email) {
        String emailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{3}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            Map<String, String> map = Collections.singletonMap("message", "올바르지 않는 형식입니다");
            return map;
        }

        return DuplicateEmailCheck.getMessage(memberRepository.existsByEmail(email));
    }

    public MemberResponse findByUserIdOrEmail(String userId, String email) {
        Member member = memberRepository.findByUserIdOrEmail(userId, email).orElseThrow(() -> new MemberNotFoundException());

        return MemberResponse.entityToDto(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void signup(MemberDto memberDto) {
        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .name(memberDto.getName())
                .phone(memberDto.getPhone())
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode(memberDto.getAddress().getPostcode())
                        .basic_address(memberDto.getAddress().getBasic_address())
                        .detail_address(memberDto.getAddress().getDetail_address())
                        .build())
                .build();

        memberRepository.save(member);
    }

    public void edit(MemberDto memberDto) {
        Member member = memberRepository.findByUserId(memberDto.getUserId()).orElseThrow(() -> new MemberNotFoundException());
        member = Member.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .name(member.getName())
                .phone(memberDto.getPhone())
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode(memberDto.getAddress().getPostcode())
                        .basic_address(memberDto.getAddress().getBasic_address())
                        .detail_address(memberDto.getAddress().getDetail_address())
                        .build())
                .build();

        memberRepository.save(member);
    }

}
