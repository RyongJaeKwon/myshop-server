package com.kwon.myshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.dto.AddressDto;
import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.repository.MemberRepository;
import com.kwon.myshop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void createTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("33333")
                .basic_address("서울")
                .detail_address("노량진")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("songsong1")
                .password(("dkfLthdgo@43"))
                .email("kaka@naver.com")
                .name("와따마")
                .phone("010-111 1-2222")
                .address(addressDto)
                .build();

        String json = objectMapper.writeValueAsString(memberDto);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/member/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());

        Member savedMember = memberRepository.findByUserId(memberDto.getUserId()).get();
        Assertions.assertEquals(memberDto.getName(), savedMember.getName());
        Assertions.assertEquals(memberDto.getUserId(), savedMember.getUserId());
        Assertions.assertEquals(memberDto.getEmail(), savedMember.getEmail());
        Assertions.assertEquals(memberDto.getPhone(), savedMember.getPhone());
        Assertions.assertEquals(memberDto.getAddress().getBasic_address(), savedMember.getAddress().getBasic_address());
        Assertions.assertEquals(memberDto.getAddress().getDetail_address(), savedMember.getAddress().getDetail_address());
        Assertions.assertEquals(memberDto.getAddress().getPostcode(), savedMember.getAddress().getPostcode());
    }

    @Test
    @DisplayName("회원가입 테스트 - 필드 유효성 검증")
    public void create2Test() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("12353")
                .basic_address("남구")
                .detail_address("")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("kkk kk123")
                .password(("sdfgst wer@43"))
                .email("qwik@na ver.com")
                .name("와 따마")
                .phone("010-1111-22 22")
                .address(addressDto)
                .build();

        String json = objectMapper.writeValueAsString(memberDto);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/member/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원수정 테스트")
    public void updateTest() throws Exception {
        // given
        AddressDto addressDto = AddressDto.builder()
                .postcode("5324132312")
                .basic_address("사상구")
                .detail_address("사상사상")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("cvxbdsgs324")
                .password(("rytdhfsxB@cv2"))
                .email("sfdsaxvz@naver.com")
                .name("리액트")
                .phone("010-3333-2222")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        AddressDto updateAddress = AddressDto.builder()
                .postcode("52343")
                .basic_address("양산")
                .detail_address("들들들들")
                .build();

        MemberDto updateMemberDto = MemberDto.builder()
                .userId("cvxbdsgs324")                  // 변경 불가
                .password(("Fsadfn@43"))
                .email("opijkl@na ver.com")
                .name("리액트")                          // 변경 불가
                .phone("010-2222-22 22")
                .address(updateAddress)
                .build();

        String json = objectMapper.writeValueAsString(updateMemberDto);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.put("/member/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원수정 - 수정데이터 정규표현식")
    public void update2Test() throws Exception {
        // given
        AddressDto addressDto = AddressDto.builder()
                .postcode("32131")
                .basic_address("사상구")
                .detail_address("사상사상")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("cvxbdsgs324")
                .password(("rytdhfsxB@cv2"))
                .email("sfdsaxvz@naver.com")
                .name("리액트")
                .phone("010-3333-2222")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        AddressDto updateAddress = AddressDto.builder()
                .postcode("5ㄱ343")
                .basic_address("양산")
                .detail_address("들들들들")
                .build();

        MemberDto updateMemberDto = MemberDto.builder()
                .userId("cvxbdsgs324")                  // 변경 불가
                .password(("Fsadfn@43"))
                .email("opijkl.naver.com")
                .name("리액트")                          // 변경 불가
                .phone("010-2222.2222")
                .address(updateAddress)
                .build();

        String json = objectMapper.writeValueAsString(updateMemberDto);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.put("/member/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400 BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.phone").value("올바르지 않는 번호 형식입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.email").value("이메일 형식을 맞춰주세요"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원조회 테스트")
    public void getTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("41342")
                .basic_address("만덕구")
                .detail_address("육교건너길")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("nmuionjk42")
                .password(("adsfV@cv2"))
                .email("rkskekfk1@naver.com")
                .name("하이센")
                .phone("010-1234-1234")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        Member savedMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/member/{id}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedMember.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(savedMember.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(savedMember.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(savedMember.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(savedMember.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.postcode").value(savedMember.getAddress().getPostcode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.basic_address").value(savedMember.getAddress().getBasic_address()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.detail_address").value(savedMember.getAddress().getDetail_address()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원삭제 테스트")
    public void deleteTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("31231")
                .basic_address("하이요")
                .detail_address("하하이이")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("123asfd")
                .password(("adttV@cv2"))
                .email("tttt@naver.com")
                .name("티티티")
                .phone("010-7546-8888")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        Member savedMember = memberRepository.findByUserId(memberDto.getUserId()).get();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/member/{id}", savedMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

}
