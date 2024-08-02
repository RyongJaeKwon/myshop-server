package com.kwon.myshop.controller;

import com.kwon.myshop.dto.OrderCreateDto;
import com.kwon.myshop.dto.OrderDto;
import com.kwon.myshop.dto.OrderItemDto;
import com.kwon.myshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("#orderCreateDto.userId == principal.username")
    @PostMapping("/")
    public Map<String, Long> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        Long orderId = orderService.createOrder(orderCreateDto);

        return Map.of("RESULT", orderId);
    }

    @PreAuthorize("#orderCreateDto.userId == principal.username")
    @PostMapping("/cart")
    public Map<String, Long> createOrderFromCart(@RequestBody OrderCreateDto orderCreateDto) {
        Long orderId = orderService.createOrderFromCart(orderCreateDto);

        return Map.of("RESULT", orderId);
    }

    @PreAuthorize("#userId == principal.username")
    @GetMapping("/{userId}")
    public List<OrderDto> getOrders(@PathVariable String userId) {
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);

        return orders;
    }

    @PreAuthorize("#userId == principal.username")
    @GetMapping("/{userId}/{orderId}")
    public List<OrderItemDto> getOrderItems(@PathVariable String userId, @PathVariable Long orderId) {
        return orderService.getOrderItems(userId, orderId);
    }

    @PreAuthorize("#userId == principal.username")
    @PostMapping("/{userId}/{orderId}")
    public Map<String, String> cancelOrder(@PathVariable String userId, @PathVariable Long orderId) {
        orderService.cancelOrder(orderId);

        return Map.of("RESULT", "SUCCESS");
    }

}
