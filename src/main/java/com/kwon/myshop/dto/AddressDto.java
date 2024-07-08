package com.kwon.myshop.dto;

import com.kwon.myshop.domain.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDto {

    @NotBlank(message = "우편번호를 입력해주세요")
    @Pattern(regexp = "^\\d{5}$", message = "숫자만 입력할 수 있습니다")
    private String postcode;

    @NotBlank(message = "기본 주소를 입력해주세요")
    @Size(max = 50, message = "주소 길이는 50자를 넘을 수 없습니다.")
    private String basic_address;

    @NotBlank(message = "상세 주소를 입력해주세요")
    @Size(max = 50, message = "주소 길이는 50자를 넘을 수 없습니다.")
    private String detail_address;

    public static AddressDto fromEntity(Address address) {
        return AddressDto.builder()
                .postcode(address.getPostcode().replaceAll("\\s", ""))
                .basic_address(address.getBasic_address())
                .detail_address(address.getDetail_address())
                .build();
    }
}
