package com.kwon.myshop.service;

import com.kwon.myshop.domain.*;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.dto.OrderCreateDto;
import com.kwon.myshop.dto.OrderDto;
import com.kwon.myshop.dto.OrderItemDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    public Long createOrder(OrderCreateDto orderCreateDto) {
        Member member = memberRepository.findByUserId(orderCreateDto.getUserId()).orElseThrow(MemberNotFoundException::new);

        List<OrderItem> orderItems = orderCreateDto.getOrderItems().stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.getItemId()).orElseThrow(ItemNotFoundException::new);

                    return OrderItem.builder()
                            .item(item)
                            .orderPrice(dto.getPrice())
                            .quantity(dto.getQuantity())
                            .build();
                }).collect(Collectors.toList());

        int totalPrice = orderItems.stream()
                .mapToInt(orderItem -> orderItem.getOrderPrice() * orderItem.getQuantity()).sum();

        Address deliveryAddress = Address.builder()
                .postcode(orderCreateDto.getPostcode())
                .basic_address(orderCreateDto.getBasicAddress())
                .detail_address(orderCreateDto.getDetailAddress())
                .build();

        Delivery delivery = Delivery.builder()
                .status(DeliveryStatus.DELIVERY_READY)
                .address(deliveryAddress)
                .receiverName(orderCreateDto.getReceiverName())
                .receiverPhone(orderCreateDto.getReceiverPhone())
                .message(orderCreateDto.getMessage())
                .build();

        Order order = Order.builder()
                .member(member)
                .orderItems(orderItems)
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    public Long createOrderFromCart(OrderCreateDto orderCreateDto) {
        Member member = memberRepository.findByUserId(orderCreateDto.getUserId()).orElseThrow(MemberNotFoundException::new);

        List<CartItemListDto> cartItemList = cartItemRepository.getCartItemListByUserId(orderCreateDto.getUserId());

        List<OrderItem> orderItems = cartItemList.stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.getItemId()).orElseThrow(ItemNotFoundException::new);

                    return OrderItem.builder()
                            .item(item)
                            .orderPrice(dto.getPrice())
                            .quantity(dto.getQuantity())
                            .build();
                }).collect(Collectors.toList());

        int totalPrice = orderItems.stream()
                .mapToInt(orderItem -> orderItem.getOrderPrice() * orderItem.getQuantity()).sum();

        Address deliveryAddress = Address.builder()
                .postcode(orderCreateDto.getPostcode())
                .basic_address(orderCreateDto.getBasicAddress())
                .detail_address(orderCreateDto.getDetailAddress())
                .build();

        Delivery delivery = Delivery.builder()
                .status(DeliveryStatus.DELIVERY_READY)
                .address(deliveryAddress)
                .receiverName(orderCreateDto.getReceiverName())
                .receiverPhone(orderCreateDto.getReceiverPhone())
                .message(orderCreateDto.getMessage())
                .build();

        Order order = Order.builder()
                .member(member)
                .orderItems(orderItems)
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();

        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        List<Long> itemIds = cartItemList.stream().map(CartItemListDto::getItemId).collect(Collectors.toList());
        cartItemRepository.deleteByItemIds(itemIds);

        return savedOrder.getId();
    }

    public List<OrderDto> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);

        return orders.stream().map(order ->
                new OrderDto(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getOrderItems().stream().map(oi ->
                                new OrderDto.OrderItemDto(
                                        oi.getItem().getId(),
                                        oi.getItem().getItemName()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());

    }

    public List<OrderItemDto> getOrderItems(Long orderId) {
        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

}
