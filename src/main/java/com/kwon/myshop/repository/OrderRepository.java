package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Order;
import com.kwon.myshop.dto.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select new com.kwon.myshop.dto.OrderDto(" +
            "o.id, " +
            "oi.item.id, " +
            "oi.item.itemName, " +
            "oi.item.size, " +
            "oi.item.color, " +
            "oi.item.brand, " +
            "oi.quantity, " +
            "ii.fileName, " +
            "o.orderDate, " +
            "o.totalPrice, " +
            "o.status) " +
            "from Order o " +
            "join o.orderItems oi " +
            "left join oi.item.imageList ii " +
            "where oi.id = (select oi2.id from OrderItem oi2 where oi2.order.id = o.id order by oi2.price desc limit 1) " +
            "and o.member.userId = :userId and ii.ord = 0")
    List<OrderDto> findOrdersByUserId(@Param("userId") String userId);

}
