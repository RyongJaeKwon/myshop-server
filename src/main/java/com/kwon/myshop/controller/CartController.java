package com.kwon.myshop.controller;

import com.kwon.myshop.dto.CartItemDto;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("#cartItemDto.userId == principal.username")
    @PostMapping("/")
    public List<CartItemListDto> changeCart(@RequestBody CartItemDto cartItemDto) {

        return cartService.create(cartItemDto);
    }

    @GetMapping("/list")
    public List<CartItemListDto> list(Principal principal) {
        String userId = principal.getName();

        return cartService.getCartItems(userId);
    }

    @DeleteMapping("/{cartItemId}")
    public List<CartItemListDto> delete(@PathVariable Long cartItemId) {

        return cartService.delete(cartItemId);
    }
}
