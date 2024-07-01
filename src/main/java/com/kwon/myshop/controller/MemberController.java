package com.kwon.myshop.controller;

import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.dto.MemberResponse;
import com.kwon.myshop.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/")
    public void create(@Valid @RequestBody MemberDto memberDto) {
        memberService.create(memberDto);
    }

    @GetMapping("/{userId}/check")
    public Map<String, String> idCheck(@PathVariable @NotBlank(message = "아이디를 입력해주세요") String userId) {
        return memberService.checkUserIdDuplicate(userId);
    }

    @GetMapping("/{phone}/check")
    public Map<String, String> telCheck(@PathVariable @NotBlank(message = "번호를 입력해주세요") String phone) {
        return memberService.checkPhoneDuplicate(phone);
    }

    @GetMapping("/{email}/check")
    public Map<String, String> emailCheck(@PathVariable @NotBlank(message = "이메일을 입력해주세요") String email) {
        return memberService.checkEmailDuplicate(email);
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable Long id) {
        return memberService.get(id);
    }

    @PutMapping("/")
    public void update(@Valid @RequestBody MemberDto memberDto) {
        memberService.update(memberDto);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        memberService.delete(id);

        return Map.of("RESULT", "SUCCESS");
    }
}
