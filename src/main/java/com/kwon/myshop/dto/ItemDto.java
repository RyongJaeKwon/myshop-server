package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
public class ItemDto {

    private Long id;
    private String itemName;
    private String color;
    private String size;
    private String itemInfo;
    private String brand;
    private int price;
    private String category;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();
}
