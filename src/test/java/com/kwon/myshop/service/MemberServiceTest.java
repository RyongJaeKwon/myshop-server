package com.kwon.myshop.service;

import com.kwon.myshop.domain.Member;
import com.kwon.myshop.dto.AddressDto;
import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.dto.MemberResponse;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
//@Transactional
public class MemberServiceTest {
    
    @Autowired
    MemberService memberService;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Test
    @DisplayName("회원가입")
    public void createTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("11111")
                .basic_address("부산시 남구")
                .detail_address("문현동")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("gyj343")
                .password(("Rnjsdydwo@12"))
                .email("gyj343@gmail.com")
                .name("권용재")
                .phone("010-4442-5570")
                .address(addressDto)
                .build();

        //when
        memberService.create(memberDto);
        
        //then
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new MemberNotFoundException());

        Assertions.assertNotNull(member);
    }

    @Test
    @DisplayName("회원가입 실패")
    public void createFailTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("12361")
                .basic_address("부산시 동구")
                .detail_address("문동동")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("gyj343")
                .password(("rRmsdfnnm@3"))
                .email("gyj343@naver.com")
                .name("권용재")
                .phone("010-4442-5570")
                .address(addressDto)
                .build();

        //when
        memberService.create(memberDto);

        //then
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new MemberNotFoundException());

        Assertions.assertNotNull(member);
    }

    @Test
    @DisplayName("회원조회")
    public void getTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("99999")
                .basic_address("오륙도")
                .detail_address("스카이")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("qwer123")                   // 변경 불가
                .password(("rkrKopasbbd31@35"))
                .email("sksms@daum.net")
                .name("노홍철")                           // 변경 불가
                .phone("010-9999-0000")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        Member savedMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        //when
        MemberResponse memberResponse = memberService.get(savedMember.getUserId());

        //then
        Assertions.assertNotNull(memberResponse);
        Assertions.assertEquals(memberDto.getUserId(), memberResponse.getUserId());
        Assertions.assertEquals(memberDto.getEmail(), memberResponse.getEmail());
        Assertions.assertEquals(memberDto.getName(), memberResponse.getName());
        Assertions.assertEquals(memberDto.getPhone(), memberResponse.getPhone());
        Assertions.assertEquals(memberDto.getAddress().getPostcode(), memberResponse.getAddress().getPostcode());
        Assertions.assertEquals(memberDto.getAddress().getBasic_address(), memberResponse.getAddress().getBasic_address());
        Assertions.assertEquals(memberDto.getAddress().getDetail_address(), memberResponse.getAddress().getDetail_address());
    }

    @Test
    @DisplayName("회원수정")
    public void updateTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("12341")
                .basic_address("부산시 용호산")
                .detail_address("대머리산")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("gyj343")                   // 변경 불가
                .password(("rkrKopasfd31@35"))
                .email("gyj343@daum.com")
                .name("권용재")                           // 변경 불가
                .phone("010-7777-5570")
                .address(addressDto)
                .build();

        //when

        Member expectMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        memberService.update(memberDto);

        //then
        Member actualMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        Assertions.assertEquals(expectMember.getUserId(), actualMember.getUserId());
        Assertions.assertEquals(expectMember.getName(), actualMember.getName());
        Assertions.assertNotEquals(expectMember.getEmail(), actualMember.getEmail());
        Assertions.assertNotEquals(expectMember.getPhone(), actualMember.getPhone());
        Assertions.assertNotEquals(expectMember.getAddress().getPostcode(), actualMember.getAddress().getPostcode());
        Assertions.assertNotEquals(expectMember.getAddress().getBasic_address(), actualMember.getAddress().getBasic_address());
        Assertions.assertNotEquals(expectMember.getAddress().getDetail_address(), actualMember.getAddress().getDetail_address());
    }

    @Test
    @DisplayName("회원삭제")
    public void deleteTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("132345")
                .basic_address("부산시 동구")
                .detail_address("문동동")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("qqwe42")
                .password(("31dsatrew@3"))
                .email("gyj343@naver.com")
                .name("권갈량")
                .phone("010-4442-5570")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        Member savedMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        //when
        memberRepository.deleteByEmail(memberDto.getEmail());

        //then
        Assertions.assertThrows(MemberNotFoundException.class, () -> {
            memberService.get(savedMember.getUserId());
        });


    }

}
