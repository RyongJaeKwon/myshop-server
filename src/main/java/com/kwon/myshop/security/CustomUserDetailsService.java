package com.kwon.myshop.security;

import com.kwon.myshop.domain.Member;
import com.kwon.myshop.dto.MemberDetails;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("---------------------loadUserByUsername------------------------");

        Member member = memberRepository.findByUserId(username).orElseThrow(MemberNotFoundException::new);

        MemberDetails memberDetails = new MemberDetails(
                member.getId(),
                member.getUserId(),
                member.getPassword(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getAddress(),
                member.getRole().getValue()
        );

        return memberDetails;
    }
}
