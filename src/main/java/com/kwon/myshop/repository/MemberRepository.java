package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserIdOrEmail(String userId, String email);

    @Transactional
    void deleteByEmail(String email);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

}
