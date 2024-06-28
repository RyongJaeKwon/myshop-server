package com.kwon.myshop.dto;

import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String userId;
    private String email;
    private String name;
    private String phone;
    private Role role;
    private AddressDto address;


    public static MemberResponse entityToDto(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getRole())
                .address(AddressDto.fromEntity(member.getAddress()))
                .build();
    }

}