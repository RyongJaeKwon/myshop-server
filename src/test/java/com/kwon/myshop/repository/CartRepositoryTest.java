package com.kwon.myshop.repository;

import com.kwon.myshop.domain.*;
import com.kwon.myshop.dto.CartItemListDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("장바구니 아이템 추가")
    @Transactional
    public void InsertItemTest() {
        //given
        Member member = Member.builder()
                .userId("testUser")
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phone("010-4541-8783")
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode("12345")
                        .basic_address("금정구")
                        .detail_address("금정산성").build())
                .build();
        Member savedMember = memberRepository.save(member);

        Item item = Item.builder()
                .itemName("아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);


        //when
        CartItem cartItem = cartItemRepository.getCartItemByItemId(savedMember.getUserId(), item.getId());

        if (cartItem != null) {
            cartItem.changeQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);

            return;
        }

        Optional<Cart> result = cartRepository.getCartByMember(savedMember.getUserId());

        Cart cart = null;
        if (result.isEmpty()) {
            Cart tempCart = Cart.builder()
                    .member(savedMember)
                    .build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .item(savedItem)
                    .cart(cart)
                    .quantity(1)
                    .build();
        }
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        //then
        Assertions.assertNotNull(savedCartItem);
        Assertions.assertEquals(savedCartItem.getCart().getId(), cart.getId());
        Assertions.assertEquals(savedCartItem.getItem().getId(), savedItem.getId());
        Assertions.assertTrue(savedCartItem.getQuantity() == 1);
    }

    @Test
    @DisplayName("기존 장바구니 아이템 수량 증가")
    @Transactional
    public void AddQuantityTest() {
        //given
        Member member = Member.builder()
                .userId("testUser")
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phone("010-4541-8783")
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode("12345")
                        .basic_address("금정구")
                        .detail_address("금정산성").build())
                .build();
        Member savedMember = memberRepository.save(member);

        Item item = Item.builder()
                .itemName("아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);

        Cart savedCart = cartRepository.save(Cart.builder().member(savedMember).build());

        CartItem savedCartItem = cartItemRepository.save(CartItem.builder().item(savedItem).cart(savedCart).quantity(1).build());


        //when
        savedCartItem.changeQuantity(savedCartItem.getQuantity() + 1);
        CartItem updateCartItem = cartItemRepository.save(savedCartItem);

        //then
        Assertions.assertNotNull(updateCartItem);
        Assertions.assertEquals(updateCartItem.getCart().getId(), savedCartItem.getCart().getId());
        Assertions.assertEquals(updateCartItem.getItem().getId(), savedCartItem.getItem().getId());
        Assertions.assertTrue(updateCartItem.getQuantity() == savedCartItem.getQuantity());
    }

    @Test
    @DisplayName("장바구니 아이템 목록 조회")
    @Transactional
    public void getCartItemListTest() {
        //given
        Member member = Member.builder()
                .userId("testUser")
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phone("010-4541-8783")
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode("12345")
                        .basic_address("금정구")
                        .detail_address("금정산성").build())
                .build();
        Member savedMember = memberRepository.save(member);

        Item item1 = Item.builder()
                .itemName("아이템1")
                .itemInfo("아이템 정보1")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        item1.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item1.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem1 = itemRepository.save(item1);

        Item item2 = Item.builder()
                .itemName("아이템2")
                .itemInfo("아이템 정보2")
                .price(32000)
                .category("bottom")
                .color("black")
                .size("XLarge")
                .brand("adidas")
                .build();
        item2.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item2.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem2 = itemRepository.save(item2);

        Item item3 = Item.builder()
                .itemName("아이템3")
                .itemInfo("아이템 정보3")
                .price(19000)
                .category("Hat")
                .color("gray")
                .size("free")
                .brand("newbal")
                .build();
        item3.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item3.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem3 = itemRepository.save(item3);

        Cart savedCart = cartRepository.save(Cart.builder().member(savedMember).build());

        cartItemRepository.save(CartItem.builder().item(savedItem1).cart(savedCart).quantity(1).build());
        cartItemRepository.save(CartItem.builder().item(savedItem2).cart(savedCart).quantity(2).build());
        cartItemRepository.save(CartItem.builder().item(savedItem3).cart(savedCart).quantity(1).build());

        //when
        List<CartItemListDto> cartItemList = cartItemRepository.getCartItemListByUserId(savedMember.getUserId());

        //then
        Assertions.assertNotNull(cartItemList);
        Assertions.assertEquals(3, cartItemList.size());

        CartItemListDto cartItemListDto1 = cartItemList.get(0);
        Assertions.assertEquals(savedItem3.getId(), cartItemListDto1.getItemId());

        CartItemListDto cartItemListDto2 = cartItemList.get(1);
        Assertions.assertEquals(savedItem2.getId(), cartItemListDto2.getItemId());

        CartItemListDto cartItemListDto3 = cartItemList.get(2);
        Assertions.assertEquals(savedItem1.getId(), cartItemListDto3.getItemId());
    }

    @Test
    @DisplayName("장바구니 아이템 삭제")
    @Transactional
    public void deleteCartItemListTest() {
        //given
        Member member = Member.builder()
                .userId("testUser")
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phone("010-4541-8783")
                .role(Role.MEMBER)
                .address(Address.builder()
                        .postcode("12345")
                        .basic_address("금정구")
                        .detail_address("금정산성").build())
                .build();
        Member savedMember = memberRepository.save(member);

        Item item1 = Item.builder()
                .itemName("아이템1")
                .itemInfo("아이템 정보1")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        item1.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item1.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem1 = itemRepository.save(item1);

        Item item2 = Item.builder()
                .itemName("아이템2")
                .itemInfo("아이템 정보2")
                .price(32000)
                .category("bottom")
                .color("black")
                .size("XLarge")
                .brand("adidas")
                .build();
        item2.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item2.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem2 = itemRepository.save(item2);

        Item item3 = Item.builder()
                .itemName("아이템3")
                .itemInfo("아이템 정보3")
                .price(19000)
                .category("Hat")
                .color("gray")
                .size("free")
                .brand("newbal")
                .build();
        item3.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item3.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem3 = itemRepository.save(item3);

        Cart savedCart = cartRepository.save(Cart.builder().member(savedMember).build());

        cartItemRepository.save(CartItem.builder().item(savedItem1).cart(savedCart).quantity(1).build());
        cartItemRepository.save(CartItem.builder().item(savedItem2).cart(savedCart).quantity(2).build());
        cartItemRepository.save(CartItem.builder().item(savedItem3).cart(savedCart).quantity(1).build());

        //when
        List<CartItemListDto> cartItemList = cartItemRepository.getCartItemListByUserId(savedMember.getUserId());

        // 해당 장바구니 아이템을 삭제하기 전에 장바구니 번호를 구한다
        Long cartId = cartItemRepository.getCartByCartItemId(cartItemList.get(0).getCartItemId());

        // 장바구니 아이템 삭제
        cartItemRepository.deleteById(cartItemList.get(0).getCartItemId());

        // 구해놨던 장바구니 번호를 이용해서 삭제된 후 장바구니 아이템 목록을 구한다
        List<CartItemListDto> afterCartItemList = cartItemRepository.getCartItemListDtoByCart(cartId);

        //then
        Assertions.assertNotNull(afterCartItemList);
        Assertions.assertEquals(2, afterCartItemList.size());

        CartItemListDto cartItemListDto1 = afterCartItemList.get(0);
        Assertions.assertEquals(savedItem2.getId(), cartItemListDto1.getItemId());

        CartItemListDto cartItemListDto2 = afterCartItemList.get(1);
        Assertions.assertEquals(savedItem1.getId(), cartItemListDto2.getItemId());
    }

}
