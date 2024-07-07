package com.kwon.myshop.service;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.ItemImage;
import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.dto.PageRequestDto;
import com.kwon.myshop.dto.PageResponseDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    public Long create(ItemDto itemDto) {
        Item savedItem = itemRepository.save(dtoToEntity(itemDto));
        return savedItem.getId();
    }

    public void update(ItemDto itemDto) {
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(ItemNotFoundException::new);

        item.changeItemName(itemDto.getItemName());
        item.changeColor(itemDto.getColor());
        item.changeSize(itemDto.getSize());
        item.changeItemInfo(itemDto.getItemInfo());
        item.changeBrand(itemDto.getBrand());
        item.changePrice(itemDto.getPrice());
        item.changeCategory(itemDto.getCategory());

        item.removeImages();

        List<String> uploadFileNames = itemDto.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(item::addFileName);
        }

        itemRepository.save(item);
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    public ItemDto getItemWithImages(Long id) {
        Item item = itemRepository.findByIdWithImages(id).orElseThrow(ItemNotFoundException::new);
        return entityToDto(item);
    }

    public PageResponseDto<ItemDto> getRecentItems(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
        Page<Object[]> result = itemRepository.findRecentItemList(pageable);

        int totalPages = result.getTotalPages();

        List<ItemDto> dtoList = result.get().map(res -> {
            Item item = (Item) res[0];
            ItemImage itemImage = (ItemImage) res[1];

            ItemDto itemDto = ItemDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .itemInfo(item.getItemInfo())
                    .brand(item.getBrand())
                    .price(item.getPrice())
                    .category(item.getCategory())
                    .uploadFileNames(List.of(itemImage.getFileName()))
                    .regDate(item.getRegDate())
                    .modDate(item.getModDate())
                    .build();

            return itemDto;
        }).collect(Collectors.toList());

        return PageResponseDto.<ItemDto>builder()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .totalPages(totalPages)
                .build();
    }

    public PageResponseDto<ItemDto> getRecentItemsWithCategory(PageRequestDto pageRequestDto, String category) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
        Page<Object[]> result = itemRepository.findRecentItemListByCategory(pageable, category);

        int totalPages = result.getTotalPages();

        List<ItemDto> dtoList = result.get().map(res -> {
            Item item = (Item) res[0];
            ItemImage itemImage = (ItemImage) res[1];

            ItemDto itemDto = ItemDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .color(item.getColor())
                    .size(item.getSize())
                    .itemInfo(item.getItemInfo())
                    .brand(item.getBrand())
                    .price(item.getPrice())
                    .category(item.getCategory())
                    .uploadFileNames(List.of(itemImage.getFileName()))
                    .regDate(item.getRegDate())
                    .modDate(item.getModDate())
                    .build();

            return itemDto;
        }).collect(Collectors.toList());

        return PageResponseDto.<ItemDto>builder()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .totalPages(totalPages)
                .build();
    }

    public Item dtoToEntity(ItemDto itemDto) {
        Item item = Item.builder()
                .id(itemDto.getId())
                .itemName(itemDto.getItemName())
                .color(itemDto.getColor())
                .size(itemDto.getSize())
                .itemInfo(itemDto.getItemInfo())
                .brand(itemDto.getBrand())
                .price(itemDto.getPrice())
                .category(itemDto.getCategory())
                .build();


        List<String> uploadFileNames = itemDto.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(uploadName -> {
                item.addFileName(uploadName);
            });
        }

        return item;
    }

    public ItemDto entityToDto(Item item) {
        List<String> fileNames = Optional.ofNullable(item.getImageList())
                .orElseGet(Collections::emptyList)
                .stream().map(ItemImage::getFileName).collect(Collectors.toList());

        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .color(item.getColor())
                .size(item.getSize())
                .itemInfo(item.getItemInfo())
                .brand(item.getBrand())
                .price(item.getPrice())
                .category(item.getCategory())
                .uploadFileNames(fileNames)
                .regDate(item.getRegDate())
                .modDate(item.getModDate())
                .build();

        return itemDto;
    }

}
