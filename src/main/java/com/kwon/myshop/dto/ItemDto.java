package com.kwon.myshop.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ItemDto {

    private Long id;
    private String itemName;
    private String color;
    private String size;
    private String itemInfo;
    private String brand;
    private int price;
    private String category;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();
}
