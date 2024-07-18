package com.kwon.myshop.repository;

import com.kwon.myshop.domain.CartItem;
import com.kwon.myshop.dto.CartItemListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select " +
            "new com.kwon.myshop.dto.CartItemListDto(ci.id, i.id, i.itemName, i.price, ci.quantity, ii.fileName) " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "left join Item i on ci.item = i " +
            "left join i.imageList ii " +
            "where " +
            "c.member.userId = :userId and ii.ord = 0 " +
            "order by ci.regDate desc ")
    List<CartItemListDto> getCartItemListByUserId(@Param("userId") String userId);

    @Query("select " +
            "ci " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "where " +
            "c.member.userId = :userId and ci.item.id = :itemId")
    CartItem getCartItemByItemId(@Param("userId") String userId,
                                 @Param("itemId") Long itemId);

    @Query("select " +
            "c.id " +
            "from " +
            "Cart c inner join CartItem ci on ci.cart = c " +
            "where " +
            "ci.id = :cartItemId")
    Long getCartByCartItemId(@Param("cartItemId") Long cartItemId);

    @Query("select " +
            "new com.kwon.myshop.dto.CartItemListDto(ci.id, i.id, i.itemName, i.price, ci.quantity, ii.fileName) " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "left join Item i on ci.item = i " +
            "left join i.imageList ii " +
            "where " +
            "c.id = :id and ii.ord = 0 " +
            "order by ci.regDate desc ")
    List<CartItemListDto> getCartItemListDtoByCart(@Param("id") Long id);
}
