package com.kwon.myshop.repository;

import com.kwon.myshop.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;
    @Autowired OrderItemRepository orderItemRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;

    private Member createMember(String userId) {
        return memberRepository.save(Member.builder()
                .userId(userId)
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phone("010-4541-8783")
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode("12345")
                        .basic_address("금정구")
                        .detail_address("금정산성").build())
                .build());
    }

    private Item createItem(String itemName, String itemInfo, int price, String category, String color, String size, String brand) {
        Item item = Item.builder()
                .itemName(itemName)
                .itemInfo(itemInfo)
                .price(price)
                .category(category)
                .color(color)
                .size(size)
                .brand(brand)
                .build();
        item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");

        return itemRepository.save(item);
    }

    private OrderItem createOrderItem(Item item, int quantity) {
        return orderItemRepository.save(OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .quantity(quantity)
                .build());
    }

    private Delivery createDelivery(Member member) {
        return Delivery.builder()
                .status(DeliveryStatus.DELIVERY_READY)
                .address(member.getAddress())
                .receiverName("Receiver Name")
                .receiverPhone(member.getPhone())
                .message("Test Message")
                .build();
    }

    private Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.builder()
                .member(member)
                .orderItems(List.of(orderItems))
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .totalPrice(orderItems[0].getOrderPrice() * orderItems[0].getQuantity() +
                        orderItems[1].getOrderPrice() * orderItems[1].getQuantity())
                .build();

        member.setOrder(order);

        return orderRepository.save(order);
    }

    @Test
    @DisplayName("userId로 주문 검색")
    @Transactional
    public void findOrdersByUserIdTest() throws Exception {
        //given
        Member member = createMember("testUserId");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");
        Item item2 = createItem("Item2", "Item2 info", 20000, "top", "white", "medium", "nike");
        Item item3 = createItem("Item3", "Item3 info", 23000, "bottom", "blue", "small", "mood");
        log.info("item1: " + item1);

        OrderItem orderItem1 = createOrderItem(item1, 2);
        OrderItem orderItem2 = createOrderItem(item2, 1);
        OrderItem orderItem3 = createOrderItem(item3, 1);

        Order order1 = createOrder(member, createDelivery(member), orderItem1, orderItem2);
        Order order2 = createOrder(member, createDelivery(member), orderItem2, orderItem3);
        log.info("order1: " + order1);
        log.info("order2: " + order2);
        log.info("member: " + member);

        //when
        List<Order> orders = orderRepository.findOrdersByUserId(member.getUserId());
        log.info("orders: " + orders);

        //then
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.size() == 2);
        Assertions.assertEquals(item1.getItemName(), orders.get(0).getOrderItems().get(0).getItem().getItemName());
        Assertions.assertEquals(item2.getItemName(), orders.get(0).getOrderItems().get(1).getItem().getItemName());
        Assertions.assertEquals(item2.getItemName(), orders.get(1).getOrderItems().get(0).getItem().getItemName());
        Assertions.assertEquals(item3.getItemName(), orders.get(1).getOrderItems().get(1).getItem().getItemName());

    }

}
