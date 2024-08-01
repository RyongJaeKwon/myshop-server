package com.kwon.myshop.service;

import com.kwon.myshop.domain.*;
import com.kwon.myshop.dto.OrderCreateDto;
import com.kwon.myshop.dto.OrderDto;
import com.kwon.myshop.dto.OrderItemDto;
import com.kwon.myshop.repository.*;
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
@Transactional
@Slf4j
public class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;

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

    private Order createOrder(Member member, Delivery delivery, OrderItem orderItem) {
        Order order = Order.builder()
                .member(member)
                .orderItems(List.of(orderItem))
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .totalPrice(orderItem.getOrderPrice() * orderItem.getQuantity())
                .build();

        member.setOrder(order);

        return orderRepository.save(order);
    }

    @Test
    @DisplayName("주문 생성")
    @Transactional
    public void createOrderTest() throws Exception {
        //given
        Member member = createMember("user1");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId("user1")
                .itemId(item1.getId())
                .price(item1.getPrice())
                .quantity(2)
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        //when
        orderService.createOrder(orderCreateDto);

        //then
        List<Order> savedOrders = orderRepository.findOrdersByUserId(member.getUserId());

        Assertions.assertNotNull(savedOrders);
        Assertions.assertEquals(50000, savedOrders.get(0).getTotalPrice());
        Assertions.assertEquals(OrderStatus.ORDER, savedOrders.get(0).getStatus());
    }

    @Test
    @DisplayName("장바구니에서 주문 생성")
    @Transactional
    public void createOrderFromCartTest() throws Exception {
        //given
        Member member = createMember("user1");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");
        Item item2 = createItem("Item2", "Item2 info", 20000, "top", "white", "medium", "nike");

        Cart cart = cartRepository.save(Cart.builder().member(member).build());

        CartItem cartItem1 = CartItem.builder()
                .item(item1)
                .cart(cart)
                .quantity(2)
                .build();
        cartItemRepository.save(cartItem1);

        CartItem cartItem2 = CartItem.builder()
                .item(item2)
                .cart(cart)
                .quantity(1)
                .build();
        cartItemRepository.save(cartItem2);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId("user1")
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        //when
        orderService.createOrderFromCart(orderCreateDto);

        //then
        List<Order> savedOrders = orderRepository.findOrdersByUserId(member.getUserId());

        Assertions.assertNotNull(savedOrders);
        Assertions.assertEquals(70000, savedOrders.get(0).getTotalPrice());
        Assertions.assertEquals(OrderStatus.ORDER, savedOrders.get(0).getStatus());
        Assertions.assertEquals(2, savedOrders.get(0).getOrderItems().size());
    }

    @Test
    @DisplayName("userId로 주문 검색")
    @Transactional
    public void getOrdersByUserIdTest() throws Exception {
        //given
        Member member = createMember("user1");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");
        Item item2 = createItem("Item2", "Item2 info", 20000, "top", "white", "medium", "nike");
        Item item3 = createItem("Item3", "Item3 info", 23000, "bottom", "blue", "small", "mood");

        OrderCreateDto orderCreateDto1 = OrderCreateDto.builder()
                .userId(member.getUserId())
                .itemId(item1.getId())
                .price(item1.getPrice())
                .quantity(1)
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        orderService.createOrder(orderCreateDto1);

        Cart cart = cartRepository.save(Cart.builder().member(member).build());

        CartItem cartItem1 = CartItem.builder()
                .item(item3)
                .cart(cart)
                .quantity(2)
                .build();
        cartItemRepository.save(cartItem1);

        CartItem cartItem2 = CartItem.builder()
                .item(item2)
                .cart(cart)
                .quantity(1)
                .build();
        cartItemRepository.save(cartItem2);

        OrderCreateDto orderCreateDto2 = OrderCreateDto.builder()
                .userId(member.getUserId())
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        orderService.createOrderFromCart(orderCreateDto2);

        //when
        List<OrderDto> savedOrders = orderService.getOrdersByUserId(member.getUserId());

        //then

        Assertions.assertNotNull(savedOrders);
        Assertions.assertEquals(66000, savedOrders.get(0).getTotalPrice());
        Assertions.assertEquals(25000, savedOrders.get(1).getTotalPrice());
    }

    @Test
    @DisplayName("orderId로 주문 상세 검색")
    @Transactional
    public void getOrderItemsByUserIdTest() throws Exception {
        //given
        Member member = createMember("user1");
        Member member2 = createMember("user2");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");
        Item item2 = createItem("Item2", "Item2 info", 20000, "top", "white", "medium", "nike");
        Item item3 = createItem("Item3", "Item3 info", 23000, "bottom", "blue", "small", "mood");

        OrderCreateDto orderCreateDto1 = OrderCreateDto.builder()
                .userId(member.getUserId())
                .itemId(item2.getId())
                .price(item2.getPrice())
                .quantity(2)
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        Long orderId1 = orderService.createOrder(orderCreateDto1);

        Cart cart = cartRepository.save(Cart.builder().member(member2).build());

        CartItem cartItem1 = CartItem.builder()
                .item(item3)
                .cart(cart)
                .quantity(2)
                .build();
        cartItemRepository.save(cartItem1);

        CartItem cartItem2 = CartItem.builder()
                .item(item2)
                .cart(cart)
                .quantity(1)
                .build();
        cartItemRepository.save(cartItem2);

        OrderCreateDto orderCreateDto2 = OrderCreateDto.builder()
                .userId(member2.getUserId())
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        Long orderId2 = orderService.createOrderFromCart(orderCreateDto2);

        //when
        List<OrderItemDto> orderItemDtos = orderItemRepository.findOrderItemsByOrderId(orderCreateDto1.getUserId(), orderId1);
        log.info("orderId1: " + orderId1);
        log.info("orderItemDtos: " + orderItemDtos);
        List<OrderItemDto> orderItemDtos2 = orderItemRepository.findOrderItemsByOrderId(orderCreateDto2.getUserId(), orderId2);
        log.info("orderId2: " + orderId2);
        log.info("orderItemDtos2: " + orderItemDtos2);

        //then
        Assertions.assertNotNull(orderItemDtos);
        Assertions.assertNotNull(orderItemDtos2);
        Assertions.assertEquals(1, orderItemDtos.size());
        Assertions.assertEquals(2, orderItemDtos2.size());

        // order에서 orderItem 가격 높은순으로 정렬
        Assertions.assertEquals(item2.getId(), orderItemDtos.get(0).getItemId());
        Assertions.assertEquals(item3.getId(), orderItemDtos2.get(0).getItemId());
        Assertions.assertEquals(item2.getId(), orderItemDtos2.get(1).getItemId());
    }

    @Test
    @DisplayName("주문 취소")
    @Transactional
    public void cancelOrderTest() throws Exception {
        //given
        Member member = createMember("user1");

        Item item1 = createItem("Item1", "Item1 info", 25000, "hat", "black", "large", "mlb");

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(member.getUserId())
                .itemId(item1.getId())
                .price(item1.getPrice())
                .quantity(2)
                .postcode("12345")
                .basicAddress("Basic Address")
                .detailAddress("Detail Address")
                .receiverName("John Doe")
                .receiverPhone("123-456-7890")
                .message("Message")
                .build();

        Long orderId = orderService.createOrder(orderCreateDto);

        //when
        orderService.cancelOrder(orderId);

        //then
        List<OrderDto> orders = orderService.getOrdersByUserId(member.getUserId());

        Assertions.assertNotNull(orders);
        Assertions.assertEquals(OrderStatus.CANCEL, orders.get(0).getStatus());
    }
}
