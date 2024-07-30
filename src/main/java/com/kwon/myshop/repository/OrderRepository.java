package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems", "orderItems.item"})
    @Query("select o from Order o where o.member.userId = :userId order by o.orderDate desc")
    List<Order> findOrdersByUserId(@Param("userId") String userId);

}
