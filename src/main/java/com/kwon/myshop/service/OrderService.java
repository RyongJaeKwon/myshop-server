package com.kwon.myshop.service;

import com.kwon.myshop.domain.*;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.dto.OrderCreateDto;
import com.kwon.myshop.dto.OrderDto;
import com.kwon.myshop.dto.OrderItemDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.exception.OrderNotFoundException;
import com.kwon.myshop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    public Long createOrder(OrderCreateDto orderCreateDto) {
        Member member = memberRepository.findByUserId(orderCreateDto.getUserId()).orElseThrow(MemberNotFoundException::new);
        Item item = itemRepository.findById(orderCreateDto.getItemId()).orElseThrow(ItemNotFoundException::new);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderCreateDto.getPrice())
                .quantity(orderCreateDto.getQuantity())
                .build();

        int totalPrice = orderItem.getOrderPrice() * orderItem.getQuantity();

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
                .orderItems(List.of(orderItem))
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();

        orderItem.setOrder(order);

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
        log.info("orders: " + orders);

        return orders.stream().map(order -> OrderDto.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .items(order.getOrderItems().stream().map(orderItem -> OrderDto.OrderItemDto.builder()
                        .itemId(orderItem.getItem().getId())
                        .itemName(orderItem.getItem().getItemName())
                        .build()
                ).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

    }

    public List<OrderItemDto> getOrderItems(String userId, Long orderId) {
        return orderItemRepository.findOrderItemsByOrderId(userId, orderId);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        order.cancel();
    }

}
