package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

    @Query("select case when count(m) > 0 then true else false end from Member m where m.phone = :phone")
    boolean existsByPhone(@Param("phone") String phone);
}
