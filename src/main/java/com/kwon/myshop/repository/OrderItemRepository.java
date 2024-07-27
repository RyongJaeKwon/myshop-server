package com.kwon.myshop.repository;

import com.kwon.myshop.domain.OrderItem;
import com.kwon.myshop.dto.OrderItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select " +
            "new com.kwon.myshop.dto.OrderItemDto(oi.item.id, oi.item.itemName, oi.item.color, oi.item.size, oi.price, oi.quantity, ii.fileName) " +
            "from OrderItem oi " +
            "left join oi.item.imageList ii " +
            "where oi.order.id = :orderId and ii.ord = 0")
    List<OrderItemDto> findOrderItemsByOrderId(@Param("orderId") Long orderId);

}
