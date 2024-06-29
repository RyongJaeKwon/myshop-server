package com.kwon.myshop.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwon.myshop.dto.AddressDto;
import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.security.jwt.JWTUtil;
import com.kwon.myshop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTUtilTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @MockBean
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("토큰생성 테스트")
    public void createTokenTest() throws Exception {
        //given
        AddressDto addressDto = AddressDto.builder()
                .postcode("99999")
                .basic_address("비키니시티")
                .detail_address("징징이빌딩")
                .build();

        MemberDto memberDto = MemberDto.builder()
                .userId("jingjing2")
                .password(("sadfhj@13"))
                .email("jing@naver.com")
                .name("징징이")
                .phone("010-9999-9999")
                .address(addressDto)
                .build();

        memberService.create(memberDto);

        Mockito.when(jwtUtil.createJwt(Mockito.anyMap(), Mockito.anyInt()))
                .thenReturn("agsgdg34dsgdfg3sgsdf2");

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "jingjing2");
        loginRequest.put("password", "sadfhj@13");

        String json = objectMapper.writeValueAsString(loginRequest);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        //then
        String response = mvcResult.getResponse().getContentAsString();

        Assertions.assertNotNull(response);


    }

}
