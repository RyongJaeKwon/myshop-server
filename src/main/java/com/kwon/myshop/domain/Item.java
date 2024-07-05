package com.kwon.myshop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@ToString(exclude = {"imageList", "replies"})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private String color;
    private String size;
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

    public void changeItemName(String itemName) {
        this.itemName = itemName;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void changeSize(String size) {
        this.size = size;
    }

    public void changeItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public void changeBrand(String brand) {
        this.brand = brand;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeCategory(String category) {
        this.category = category;
    }

    public void addImage(ItemImage itemImage) {
        itemImage.setOrd(this.imageList.size());
        imageList.add(itemImage);
    }

    public void addFileName(String fileName) {
        ItemImage itemImage = ItemImage.builder()
                .fileName(fileName)
                .build();
        addImage(itemImage);
    }

    public void removeImages() {
        this.imageList.clear();
    }
}
