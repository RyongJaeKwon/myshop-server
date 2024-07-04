package com.kwon.myshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private String itemInfo;
    private String brand;
    private int price;
    private String category;

    @ElementCollection
    @Builder.Default
    private List<ItemImage> imageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @PreRemove
    private void removeImages() {
        imageList.clear();
    }
}
