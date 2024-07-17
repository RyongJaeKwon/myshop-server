package com.kwon.myshop.controller;

import com.kwon.myshop.dto.MemberDto;
import com.kwon.myshop.dto.MemberResponse;
import com.kwon.myshop.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @GetMapping("/user-id/{userId}/check")
    public Map<String, String> duplicateUserIdCheck(@PathVariable @NotBlank(message = "아이디를 입력해주세요") String userId) {
        return memberService.duplicateUserIdCheck(userId);
    }

    @GetMapping("/user-email/{email}/check")
    public Map<String, String> duplicateEmailCheck(@PathVariable @NotBlank(message = "이메일을 입력해주세요") String email) {
        return memberService.duplicateEmailCheck(email);
    }

    @GetMapping("/{userId}")
    public MemberResponse get(@PathVariable String userId) {
        return memberService.get(userId);
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
