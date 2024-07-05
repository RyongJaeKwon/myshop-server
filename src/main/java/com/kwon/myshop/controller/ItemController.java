package com.kwon.myshop.controller;

import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.file.FileUil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final FileUil fileUil;

    @PostMapping("/")
    public Map<String, String> create(ItemDto itemDto) {

        List<MultipartFile> files = itemDto.getFiles();
        List<String> uploadFileNames = fileUil.addFiles(files);

        ItemDto savedItemDto = ItemDto.builder()
                .uploadFileNames(uploadFileNames)
                .build();

        log.info(uploadFileNames.get(0));

        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> GetOneFile(@PathVariable String fileName) {
        return fileUil.getFile(fileName);
    }

}
