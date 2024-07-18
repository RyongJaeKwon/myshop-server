package com.kwon.myshop.service;

import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Role;
import com.kwon.myshop.dto.CartItemDto;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.repository.ItemRepository;
import com.kwon.myshop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;

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
        item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
        Item savedItem = itemRepository.save(item);

        CartItemDto cartItemDto = CartItemDto.builder()
                .userId(savedMember.getUserId())
                .itemId(savedItem.getId())
                .quantity(1)
                .build();

        //when
        List<CartItemListDto> cartItemList = cartService.create(cartItemDto);

        //then
        Assertions.assertNotNull(cartItemList);
        Assertions.assertFalse(cartItemList.isEmpty());
        Assertions.assertEquals(savedItem.getId(), cartItemList.get(0).getItemId());
    }

    @Test
    @DisplayName("장바구니 아이템 삭제")
    @Transactional
    public void deleteItemTest() {
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

        CartItemDto cartItemDto1 = CartItemDto.builder().userId(savedMember.getUserId()).itemId(savedItem1.getId()).quantity(1).build();
        CartItemDto cartItemDto2 = CartItemDto.builder().userId(savedMember.getUserId()).itemId(savedItem2.getId()).quantity(1).build();
        CartItemDto cartItemDto3 = CartItemDto.builder().userId(savedMember.getUserId()).itemId(savedItem3.getId()).quantity(1).build();

        //when
        cartService.create(cartItemDto1);
        cartService.create(cartItemDto2);
        cartService.create(cartItemDto3);

        List<CartItemListDto> cartItemList = cartService.getCartItems(savedMember.getUserId());

        cartService.delete(cartItemList.get(1).getCartItemId());

        List<CartItemListDto> newCartItemList = cartService.getCartItems(savedMember.getUserId());

        //then
        Assertions.assertTrue(newCartItemList.size() == 2);
        Assertions.assertEquals(newCartItemList.get(0).getCartItemId(), cartItemList.get(0).getCartItemId());
        Assertions.assertEquals(newCartItemList.get(1).getCartItemId(), cartItemList.get(2).getCartItemId());
    }
}
