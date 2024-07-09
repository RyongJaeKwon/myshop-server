package com.kwon.myshop.service;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.dto.PageRequestDto;
import com.kwon.myshop.dto.PageResponseDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@Slf4j
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 등록")
    @Transactional
    public void createTest() throws Exception {
        //given
        ItemDto createItemDto = ItemDto.builder()
                .itemName("아이템아이템")
                .color("빨간색")
                .size("xlarge")
                .itemInfo("긴소매")
                .brand("nike")
                .price(30000)
                .category("top")
                .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                    UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                .build();

        //when
        Long savedId = itemService.create(createItemDto);

        //then
        ItemDto saveItemDto = itemService.getItemWithImages(savedId);

        Assertions.assertNotNull(saveItemDto);
        Assertions.assertEquals(createItemDto.getItemName(), saveItemDto.getItemName());
        Assertions.assertEquals(createItemDto.getItemInfo(), saveItemDto.getItemInfo());
        Assertions.assertEquals(createItemDto.getPrice(), saveItemDto.getPrice());
        Assertions.assertEquals(createItemDto.getColor(), saveItemDto.getColor());
        Assertions.assertEquals(createItemDto.getCategory(), saveItemDto.getCategory());
        Assertions.assertEquals(createItemDto.getBrand(), saveItemDto.getBrand());
        Assertions.assertEquals(createItemDto.getSize(), saveItemDto.getSize());
        Assertions.assertTrue(createItemDto.getUploadFileNames().size() == saveItemDto.getUploadFileNames().size());
        Assertions.assertEquals(createItemDto.getUploadFileNames().get(0), saveItemDto.getUploadFileNames().get(0));
        Assertions.assertEquals(createItemDto.getUploadFileNames().get(1), saveItemDto.getUploadFileNames().get(1));
        Assertions.assertTrue(saveItemDto.getUploadFileNames().size() == 2);
    }

    @Test
    @DisplayName("아이템 수정")
    @Transactional
    public void updateTest() throws Exception {
        //given
        ItemDto createItemDto = ItemDto.builder()
                .itemName("아이템아이템")
                .color("빨간색")
                .size("xlarge")
                .itemInfo("긴소매")
                .brand("nike")
                .price(30000)
                .category("top")
                .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                        UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                .build();

        Long savedId = itemService.create(createItemDto);

        ItemDto updateItemDto = ItemDto.builder()
                .id(savedId)
                .itemName("수정아이템 수정아이템")
                .color("파랑색")
                .size("small")
                .itemInfo("나시")
                .brand("newbal")
                .price(20000)
                .category("top")
                .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test3.jpg"))
                .build();

        //when
        itemService.update(updateItemDto);

        //then
        Item saveditem = itemRepository.findById(updateItemDto.getId()).orElseThrow(ItemNotFoundException::new);
        Assertions.assertNotEquals(createItemDto.getItemName(), saveditem.getItemName());
        Assertions.assertNotEquals(createItemDto.getItemInfo(), saveditem.getItemInfo());
        Assertions.assertNotEquals(createItemDto.getPrice(), saveditem.getPrice());
        Assertions.assertNotEquals(createItemDto.getColor(), saveditem.getColor());
        Assertions.assertEquals(createItemDto.getCategory(), saveditem.getCategory());
        Assertions.assertNotEquals(createItemDto.getBrand(), saveditem.getBrand());
        Assertions.assertNotEquals(createItemDto.getSize(), saveditem.getSize());
        Assertions.assertFalse(createItemDto.getUploadFileNames().size() == saveditem.getImageList().size());
    }

    @Test
    @DisplayName("아이템 삭제")
    @Transactional
    public void deleteTest() throws Exception {
        //given
        ItemDto createItemDto = ItemDto.builder()
                .itemName("아이템아이템")
                .color("빨간색")
                .size("xlarge")
                .itemInfo("긴소매")
                .brand("nike")
                .price(30000)
                .category("top")
                .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                        UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                .build();

        Long savedId = itemService.create(createItemDto);

        //when
        itemService.delete(savedId);

        //then
        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemService.getItemWithImages(savedId);
        });
    }

    @Test
    @DisplayName("최근에 등록된 순으로 아이템 목록 조회")
    @Transactional
    public void findRecentItemsTest() throws Exception {
        //given
        String[] categories = {"top", "bottom", "hat", "acc", "shoes", "outer", "bag"};
        String[] colors = {"black", "gray", "white", "navy"};
        String[] sizes = {"small", "medium", "large", "xlarge"};
        String[] brands = {"nike", "adidas", "new balance", "champion", "yale"};
        int[] prices = {10000, 15000, 20000, 25000, 30000};


        Random random = new Random();
        Map<String, Integer> itemCountMap = new HashMap<>();
        int MAX_ITEMS_PER_CATEGORY = 5;

        for (int i = 0; i < 31; i++) {
            String category;

            do {
                category = categories[random.nextInt(categories.length)];
            } while (itemCountMap.getOrDefault(category, 0) >= MAX_ITEMS_PER_CATEGORY);

            itemCountMap.put(category, itemCountMap.getOrDefault(category, 0) + 1);

            String color = colors[random.nextInt(colors.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String brand = brands[random.nextInt(brands.length)];
            int price = prices[random.nextInt(prices.length)];

            ItemDto itemDto = ItemDto.builder()
                    .itemName("아이템" + i)
                    .itemInfo("아이템 정보" + i)
                    .price(price)
                    .category(category)
                    .color(color)
                    .size(size)
                    .brand(brand)
                    .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                            UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                    .build();


            itemService.create(itemDto);
        }

        //when
        List<ItemDto> result = itemService.getRecentItems();
        result.forEach(dto -> log.info(dto.toString()));

        List<ItemDto> dtoList = result;

        //then
        Assertions.assertFalse(dtoList.isEmpty());
        Assertions.assertTrue(dtoList.size() == 12);

        for (ItemDto itemDto : dtoList) {
            Assertions.assertNotNull(itemDto.getRegDate());
        }

        // 내림차순 검증
        for (int i = 0; i < dtoList.size() - 1; i++) {
            ItemDto currentItemDto = dtoList.get(i);
            ItemDto nextItemDto = dtoList.get(i + 1);

            Assertions.assertTrue(currentItemDto.getRegDate().isAfter(nextItemDto.getRegDate()) || currentItemDto.getRegDate().isEqual(nextItemDto.getRegDate()));
        }
    }

    @Test
    @DisplayName("카테고리별 최근에 등록된 순으로 아이템 목록 조회")
    @Transactional
    public void findRecentItemsWithCategoryTest() throws Exception {
        //given
        String[] categories = {"top", "bottom", "hat", "acc", "shoes", "outer", "bag"};
        String[] colors = {"black", "gray", "white", "navy"};
        String[] sizes = {"small", "medium", "large", "xlarge"};
        String[] brands = {"nike", "adidas", "new balance", "champion", "yale"};
        int[] prices = {10000, 15000, 20000, 25000, 30000};


        Random random = new Random();
        Map<String, Integer> itemCountMap = new HashMap<>();
        int MAX_ITEMS_PER_CATEGORY = 10;

        for (int i = 0; i < 64; i++) {
            String category;

            do {
                category = categories[random.nextInt(categories.length)];
            } while (itemCountMap.getOrDefault(category, 0) >= MAX_ITEMS_PER_CATEGORY);

            itemCountMap.put(category, itemCountMap.getOrDefault(category, 0) + 1);

            String color = colors[random.nextInt(colors.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String brand = brands[random.nextInt(brands.length)];
            int price = prices[random.nextInt(prices.length)];

            ItemDto itemDto = ItemDto.builder()
                    .itemName("아이템" + i)
                    .itemInfo("아이템 정보" + i)
                    .price(price)
                    .category(category)
                    .color(color)
                    .size(size)
                    .brand(brand)
                    .uploadFileNames(List.of(UUID.randomUUID().toString() + "_" + "Test1.jpg",
                            UUID.randomUUID().toString() + "_" + "Test2.jpg"))
                    .build();


            itemService.create(itemDto);
        }

        //when
        PageResponseDto<ItemDto> result = itemService.getRecentItemsWithCategory(PageRequestDto.builder().page(1).size(5).build(), "top");
        result.getDtoList().forEach(dto -> log.info(dto.toString()));

        List<ItemDto> dtoList = result.getDtoList();

        //then
        Assertions.assertFalse(dtoList.isEmpty());
        Assertions.assertTrue(dtoList.size() == 5);

        for (ItemDto itemDto : dtoList) {
            Assertions.assertNotNull(itemDto.getRegDate());
            Assertions.assertTrue(itemDto.getCategory().contains("top"));   // 페이징된 리스트의 카테고리 검증
        }

        // 내림차순 검증
        for (int i = 0; i < dtoList.size() - 1; i++) {
            ItemDto currentItemDto = dtoList.get(i);
            ItemDto nextItemDto = dtoList.get(i + 1);

            Assertions.assertTrue(currentItemDto.getRegDate().isAfter(nextItemDto.getRegDate()) || currentItemDto.getRegDate().isEqual(nextItemDto.getRegDate()));
        }
    }

}
