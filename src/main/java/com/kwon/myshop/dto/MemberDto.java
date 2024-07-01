package com.kwon.myshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z](?=.*\\d)[a-zA-Z\\d]{4,11}$", message = "올바르지 않는 형식입니다")   // 5~12자 내외
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",  message = "올바르지 않는 형식입니다")    // 최소 8~20자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개 포함
    private String password;

    @Email(message = "이메일 형식을 맞춰주세요")
    private String email;

    @NotBlank(message = "이름을 입력해주세요")
    @Pattern(regexp = "^[가-힣]{1,10}$", message = "올바르지 않는 형식입니다")
    private String name;

    @NotBlank(message = "번호를 입력해주세요")
    @Pattern(regexp = "^(010)-[0-9]{4}-[0-9]{4}$", message = "올바르지 않는 번호 형식입니다")
    private String phone;

    @Valid
    private AddressDto address;

    @Builder
    public MemberDto(String userId, String password, String email, String name, String phone, AddressDto address) {
        this.userId = userId.replaceAll("\\s", "");
        this.password = password.replaceAll("\\s", "");
        this.email = email.replaceAll("\\s", "");
        this.name = name.replaceAll("\\s", "");
        this.phone = phone.replaceAll("\\s", "");
        this.address = address;
    }
}
