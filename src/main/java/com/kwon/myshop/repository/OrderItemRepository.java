package com.kwon.myshop.repository;

import com.kwon.myshop.domain.OrderItem;
import com.kwon.myshop.dto.OrderItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select " +
            "new com.kwon.myshop.dto.OrderItemDto(oi.item.id, oi.item.itemName, oi.item.color, oi.item.size, oi.orderPrice, oi.quantity, ii.fileName, " +
            "oi.order.delivery.receiverName, oi.order.delivery.receiverPhone, oi.order.delivery.message, " +
            "oi.order.delivery.address.postcode, oi.order.delivery.address.basic_address, oi.order.delivery.address.detail_address) " +
            "from OrderItem oi " +
            "left join oi.item.imageList ii " +
            "where oi.order.member.userId = :userId and oi.order.id = :orderId and ii.ord = 0 order by oi.orderPrice desc")
    List<OrderItemDto> findOrderItemsByOrderId(@Param("userId") String userId, @Param("orderId") Long orderId);

}
