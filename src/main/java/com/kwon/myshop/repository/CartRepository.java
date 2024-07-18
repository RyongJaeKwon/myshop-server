package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.member.userId = :userId")
    Optional<Cart> getCartByMember(@Param("userId") String userId);
}
