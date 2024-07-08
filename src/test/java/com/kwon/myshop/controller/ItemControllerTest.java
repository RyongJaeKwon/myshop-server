package com.kwon.myshop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ItemControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입")
    @Transactional
    public void createTest() throws Exception {
        //given
        ItemDto createItemDto = ItemDto.builder()
                .itemName("아이템이요")
                .color("초록색")
                .size("large")
                .itemInfo("긴팔")
                .brand("nike")
                .price(27000)
                .category("top")
                .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                        UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                .build();

        String json = objectMapper.writeValueAsString(createItemDto);

        //expected
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Map<String, Long> map = objectMapper.readValue(content, new TypeReference<Map<String, Long>>() {});
        Long createItemId = map.get("RESULT");

        ItemDto savedItemDto = itemService.getItemWithImages(createItemId);

        Assertions.assertNotNull(savedItemDto);
        Assertions.assertEquals(createItemDto.getItemName(), savedItemDto.getItemName());
        Assertions.assertEquals(createItemDto.getColor(), savedItemDto.getColor());
        Assertions.assertEquals(createItemDto.getSize(), savedItemDto.getSize());
        Assertions.assertEquals(createItemDto.getItemInfo(), savedItemDto.getItemInfo());
        Assertions.assertEquals(createItemDto.getBrand(), savedItemDto.getBrand());
        Assertions.assertEquals(createItemDto.getPrice(), savedItemDto.getPrice());
        Assertions.assertEquals(createItemDto.getCategory(), savedItemDto.getCategory());
        Assertions.assertTrue(savedItemDto.getUploadFileNames().size() == 2);

    }
}
