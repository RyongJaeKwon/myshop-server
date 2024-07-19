package com.kwon.myshop.service;

import com.kwon.myshop.domain.Cart;
import com.kwon.myshop.domain.CartItem;
import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.dto.CartItemDto;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.exception.InvalidQuantityException;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.repository.CartItemRepository;
import com.kwon.myshop.repository.CartRepository;
import com.kwon.myshop.repository.ItemRepository;
import com.kwon.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public List<CartItemListDto> create(CartItemDto cartItemDto) {
        Long itemId = cartItemDto.getItemId();
        String userId = cartItemDto.getUserId();
        int quantity = cartItemDto.getQuantity();

        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.getCartItemByItemId(userId, itemId);

        if (cartItem != null) {
            if (quantity < 1) {
                throw new InvalidQuantityException();
            }
            cartItem.changeQuantity(quantity);
        } else {
            Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
            cartItem = CartItem.builder().cart(cart).item(item).quantity(quantity).build();
        }

        cartItemRepository.save(cartItem);

        return getCartItems(userId);
    }

    private Cart getOrCreateCart(String userId) {
        return cartRepository.getCartByMember(userId).orElseGet(() -> {
               Member member = memberRepository.findByUserId(userId).orElseThrow(MemberNotFoundException::new);
               Cart cart = Cart.builder().member(member).build();
               return cartRepository.save(cart);
        });
    }

    public List<CartItemListDto> getCartItems(String userId) {
        return cartItemRepository.getCartItemListByUserId(userId);
    }

    public List<CartItemListDto> delete(Long cartItemId) {
        Long cartId = cartItemRepository.getCartByCartItemId(cartItemId);
        cartItemRepository.deleteById(cartItemId);

        return cartItemRepository.getCartItemListDtoByCart(cartId);
    }

}
