package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 해당 카테고리의 아이템 엔티티와 연관된 imageList를 Eager로딩 한다
     */
    @EntityGraph(attributePaths = "imageList")
    @Query("select i from Item i where i.id = :id and i.category = :category")
    Optional<Item> selectOneWithCategoru(@Param("id") Long id);

    /**
     * 해당 카테고리의 아이템 목록 화면에서 아이템 하나당 대표이미지(ord = 0)만 가져오기
     */
    @Query("select i, ii from Item i left join i.imageList ii where ii.ord = 0 and i.category = :category")
    Page<Object[]> selectListWithCategory(Pageable pageable);

    /**
     * 아이템 엔티티와 연관된 imageList를 Eager로딩 한다
     */
    @EntityGraph(attributePaths = "imageList")
    @Query("select i from Item i where i.id = :id")
    Optional<Item> selectOne(@Param("id") Long id);

    /**
     * 가장 최근 등록된 아이템 목록(대표이미지(ord = 0)와 함께 조회
     */
    @Query("select i from Item i left join i.imageList ii where ii.ord = 0 order by i.regDate desc")
    Page<Object[]> findRecentItemList(Pageable pageable);

}
