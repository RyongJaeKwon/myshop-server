package com.kwon.myshop.service;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.ItemImage;
import com.kwon.myshop.domain.QItem;
import com.kwon.myshop.domain.Reply;
import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.dto.PageRequestDto;
import com.kwon.myshop.dto.PageResponseDto;
import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.repository.ItemRepository;
import com.kwon.myshop.repository.ReplyRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ReplyRepository replyRepository;
    private final ReplyService replyService;

    public Long create(ItemDto itemDto) {
        Item item = dtoToEntity(itemDto);
        log.info("Item.getImageList : " + item.getImageList());
        Item savedItem = itemRepository.save(item);

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

    public List<ItemDto> getRecentItems() {
        Pageable pageable = PageRequest.of(0, 6);
        List<Object[]> result = itemRepository.findRecentItemsWithImages(pageable);

        List<ItemDto> dtoList = result.stream().map(res -> {
            Item item = (Item) res[0];
            ItemImage itemImage = (ItemImage) res[1];

            List<Reply> replies = replyRepository.findByItemId(item.getId());
            List<ReplyDto> replyDtos = replies.stream().map(replyService::entityToDto).collect(Collectors.toList());

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
                    .replies(replyDtos)
                    .regDate(item.getRegDate())
                    .modDate(item.getModDate())
                    .build();

            return itemDto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    public PageResponseDto<ItemDto> getRecentItemsWithCategory(PageRequestDto pageRequestDto, String category) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
        Page<Object[]> result = itemRepository.findRecentItemListByCategory(pageable, category);

        int totalPages = result.getTotalPages();

        List<ItemDto> dtoList = result.get().map(res -> {
            Item item = (Item) res[0];
            ItemImage itemImage = (ItemImage) res[1];

            List<Reply> replies = replyRepository.findByItemId(item.getId());
            List<ReplyDto> replyDtos = replies.stream().map(replyService::entityToDto).collect(Collectors.toList());

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
                    .replies(replyDtos)
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

    public PageResponseDto<ItemDto> searchItemList(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by(Sort.Direction.DESC, "regDate"));

        QItem item = QItem.item;
        BooleanExpression expression = item.itemName.containsIgnoreCase(pageRequestDto.getKeyword());

        Page<Item> result = itemRepository.findAll(expression, pageable);

        int totalPages = result.getTotalPages();
        List<ItemDto> dtoList = result.stream().map(this::entityToDto).collect(Collectors.toList());

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

        List<Reply> replies = replyRepository.findByItemId(item.getId());
        List<ReplyDto> replyDtos = replies.stream().map(replyService::entityToDto).collect(Collectors.toList());

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
                .replies(replyDtos)
                .regDate(item.getRegDate())
                .modDate(item.getModDate())
                .build();

        return itemDto;
    }

}
