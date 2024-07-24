package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByItemId(Long itemId);
}
