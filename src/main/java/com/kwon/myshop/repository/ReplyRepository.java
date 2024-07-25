package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Reply;
import com.kwon.myshop.dto.CartItemListDto;
import com.kwon.myshop.dto.ReplyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select " +
            "new com.kwon.myshop.dto.ReplyDto(r.id, m.id, i.id, m.userId, r.content, r.regDate, r.modDate) " +
            "from " +
            "Reply r left join r.item i " +
            "left join r.member m " +
            "where " +
            "i.id = :itemId " +
            "order by r.regDate DESC")
    List<ReplyDto> getReplyListByItemId(@Param("itemId") Long itemId);

}
