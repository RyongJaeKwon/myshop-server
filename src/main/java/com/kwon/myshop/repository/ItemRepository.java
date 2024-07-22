package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    /**
     * 해당 카테고리의 아이템 엔티티와 연관된 imageList를 Eager로딩 한다
     */
    @EntityGraph(attributePaths = "imageList")
    @Query("select i from Item i where i.id = :id")
    Optional<Item> findByIdWithImages(@Param("id") Long id);

    /**
     * 해당 카테고리의 아이템 목록 화면에서 가장 최근에 등록된 아이템의 대표이미지(ord = 0)만 페이징해서 가져오기
     */
    @Query("select i, ii from Item i left join i.imageList ii where ii.ord = 0 and i.category = :category order by i.regDate desc")
    Page<Object[]> findRecentItemListByCategory(Pageable pageable, @Param("category") String category);

    /**
     * 메인화면에서 모든 카테고리 포함 가장 최근에 등록된 아이템의 대표이미지(ord = 0)만 페이징해서 가져오기
     */
    @Query("select i, ii from Item i left join i.imageList ii " +
            "where ii.ord = 0 " +
            "order by i.regDate desc")
    List<Object[]> findRecentItemsWithImages(Pageable pageable);

}
