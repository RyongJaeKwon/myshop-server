package com.kwon.myshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdate {

    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;

    @NotBlank(message = "이름을 입력해주세요")
    @Pattern(regexp = "^[가-힣]{1,10}$", message = "올바르지 않는 형식입니다")
    private String name;

    @NotBlank(message = "번호를 입력해주세요")
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$", message = "올바르지 않는 형식입니다")
    private String phone;

    @Valid
    private AddressDto address;

    private String role;

    @Builder
    public MemberUpdate(String userId, String password, String name, String phone, AddressDto address, String role) {
        this.userId = userId.replaceAll("\\s", "");
        this.name = name.replaceAll("\\s", "");
        this.phone = phone.replaceAll("\\s", "");
        if (address != null) {
            this.address = AddressDto.builder()
                    .postcode(address.getPostcode().replaceAll("\\s", ""))
                    .basic_address(address.getBasic_address())
                    .detail_address(address.getDetail_address())
                    .build();
        }
        this.role = role;
    }
}
