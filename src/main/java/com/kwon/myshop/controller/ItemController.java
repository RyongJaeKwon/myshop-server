package com.kwon.myshop.controller;

import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.dto.PageRequestDto;
import com.kwon.myshop.dto.PageResponseDto;
import com.kwon.myshop.file.FileUtil;
import com.kwon.myshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final FileUtil fileUtil;
    private final ItemService itemService;

    @PostMapping(value = "/")
    public Map<String, Long> create(ItemDto itemDto) {
        log.info("ItemDto: " + itemDto);
        List<MultipartFile> files = itemDto.getFiles();
        List<String> uploadFileNames = fileUtil.addFiles(files);

        ItemDto createItemDto = ItemDto.builder()
                .itemName(itemDto.getItemName())
                .color(itemDto.getColor())
                .size(itemDto.getSize())
                .itemInfo(itemDto.getItemInfo())
                .brand(itemDto.getBrand())
                .price(itemDto.getPrice())
                .category(itemDto.getCategory())
                .uploadFileNames(uploadFileNames)
                .build();

        log.info("createItemDto : " + createItemDto);
        Long id = itemService.create(createItemDto);

        return Map.of("RESULT", id);
    }

    @GetMapping("/")
    public List<ItemDto> list() {
        return itemService.getRecentItems();
    }

    @GetMapping("/{category}")
    public PageResponseDto<ItemDto> categoryList(PageRequestDto pageRequestDto, @PathVariable String category) {
        return itemService.getRecentItemsWithCategory(pageRequestDto, category);
    }

    @GetMapping("/{id}")
    public ItemDto read(@PathVariable Long id) {
        return itemService.getItemWithImages(id);
    }

    @PutMapping("/")
    public Map<String, String> update(@RequestBody ItemDto itemDto) {
        try {
            ItemDto savedItemDto = itemService.getItemWithImages(itemDto.getId());
            List<String> savedUploadFileNames = savedItemDto.getUploadFileNames();

            List<MultipartFile> files = itemDto.getFiles();
            List<String> recentUploadFileNames = fileUtil.addFiles(files);
            List<String> uploadFileNames = itemDto.getUploadFileNames();

            if (recentUploadFileNames != null && !recentUploadFileNames.isEmpty()) {
                uploadFileNames.addAll(recentUploadFileNames);
            }

            itemService.update(itemDto);

            if (savedUploadFileNames != null && !savedUploadFileNames.isEmpty()) {
                List<String> deleteFiles = savedUploadFileNames.stream()
                        .filter(fileName -> !uploadFileNames.contains(fileName))
                        .collect(Collectors.toList());

                fileUtil.deleteFiles(deleteFiles);
            }

            return Map.of("RESULT", "SUCCESS");
        } catch (Exception e) {
            return Map.of("RESULT", "FAILURE", "MESSAGE", e.getMessage());
        }
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        List<String> savedUploadFileNames = itemService.getItemWithImages(id).getUploadFileNames();
        itemService.delete(id);
        fileUtil.deleteFiles(savedUploadFileNames);
        return Map.of("RESULT", "SUCCESS");
    }

}
