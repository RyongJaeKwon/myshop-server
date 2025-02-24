package com.kwon.myshop.dto;

import com.kwon.myshop.domain.Address;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MemberDetails extends User {

    private Long id;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String role;
    private Address address = new Address();

    public MemberDetails(Long id, String userId, String password, String email, String name, String phone,
                         Address address, String role) {
        super(userId, "", Collections.singletonList(new SimpleGrantedAuthority(role)));

        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> valueMap = new HashMap<>();

        valueMap.put("id", id);
        valueMap.put("userId", userId);
        valueMap.put("email", email);
        valueMap.put("name", name);
        valueMap.put("phone", phone);
        valueMap.put("address", address);
        valueMap.put("role", role);

        return valueMap;
    }

}
