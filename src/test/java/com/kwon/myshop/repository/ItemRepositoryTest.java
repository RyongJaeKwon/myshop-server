package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.ItemImage;
import com.kwon.myshop.dto.ItemDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    @DisplayName("테스트를 위한 아이템 등록")
    public void saveItemWithTest() throws Exception {
        //given
        String[] categories = {"top", "bottom", "hat", "acc", "shoes", "outer", "bag"};
        String[] colors = {"black", "gray", "white", "navy"};
        String[] sizes = {"small", "medium", "large", "xlarge"};
        String[] brands = {"nike", "adidas", "new balance", "champion", "yale"};
        int[] prices = {10000, 15000, 20000, 25000, 30000};


        Random random = new Random();
        Map<String, Integer> itemCountMap = new HashMap<>();
        int MAX_ITEMS_PER_CATEGORY = 20;

        for (int i = 0; i < 140; i++) {
            String category;

            do {
                category = categories[random.nextInt(categories.length)];
            } while (itemCountMap.getOrDefault(category, 0) >= MAX_ITEMS_PER_CATEGORY);

            itemCountMap.put(category, itemCountMap.getOrDefault(category, 0) + 1);

            String color = colors[random.nextInt(colors.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String brand = brands[random.nextInt(brands.length)];
            int price = prices[random.nextInt(prices.length)];

            Item item = Item.builder()
                    .itemName("아이템" + i)
                    .itemInfo("아이템 정보" + i)
                    .price(price)
                    .category(category)
                    .color(color)
                    .size(size)
                    .brand(brand)
                    .build();
            item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
            item.addFileName("s_" + UUID.randomUUID().toString() + "_" + "aaa.jpg");
            item.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");
            item.addFileName("s_" + UUID.randomUUID().toString() + "_" + "bbb.jpg");
            item.addFileName(UUID.randomUUID().toString() + "_" + "ccc.jpg");
            item.addFileName("s_" + UUID.randomUUID().toString() + "_" + "ccc.jpg");

            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("아이템 등록")
    @Transactional
    public void saveItemTest() throws Exception {
        //given
        Item item = Item.builder()
                .itemName("아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item.addFileName(UUID.randomUUID().toString() + "_" + "ddd.jpg");

        //when
        Item createItem = itemRepository.save(item);

        //then
        Item savedItem = itemRepository.findById(createItem.getId()).orElseThrow(ItemNotFoundException::new);

        Assertions.assertNotNull(savedItem);
        Assertions.assertNotNull(savedItem.getImageList().get(0));
        Assertions.assertNotNull(savedItem.getImageList().get(1));
        Assertions.assertEquals(createItem.getId(), savedItem.getId());
        Assertions.assertEquals(createItem.getItemName(), savedItem.getItemName());
        Assertions.assertEquals(createItem.getItemInfo(), savedItem.getItemInfo());
        Assertions.assertEquals(createItem.getPrice(), savedItem.getPrice());
        Assertions.assertEquals(createItem.getColor(), savedItem.getColor());
        Assertions.assertEquals(createItem.getBrand(), savedItem.getBrand());
        Assertions.assertEquals(createItem.getCategory(), savedItem.getCategory());
        Assertions.assertEquals(createItem.getImageList().get(0).getFileName(), savedItem.getImageList().get(0).getFileName());
        Assertions.assertEquals(createItem.getImageList().get(1).getFileName(), savedItem.getImageList().get(1).getFileName());
        Assertions.assertEquals(createItem.getSize(), savedItem.getSize());
    }

    @Test
    @DisplayName("아이템 모두조회")
    @Transactional
    public void getItemListByIdTest() throws Exception {
        //given
        Item item1 = Item.builder().itemName("아이템1").itemInfo("아이템1 정보").price(10000).category("top").color("navy").size("small").brand("nike").build();
        Item item2 = Item.builder().itemName("아이템2").itemInfo("아이템2 정보").price(12000).category("bottom").color("black").size("xlarge").brand("adidas").build();
        Item item3 = Item.builder().itemName("아이템3").itemInfo("아이템3 정보").price(15000).category("bag").color("white").size("medium").brand("yale").build();
        Item item4 = Item.builder().itemName("아이템4").itemInfo("아이템4 정보").price(7000).category("hat").color("green").size("large").brand("new balance").build();
        Item item5 = Item.builder().itemName("아이템5").itemInfo("아이템5 정보").price(5000).category("acc").color("brown").size("xlarge").brand("nike").build();


        Item createItem1 = itemRepository.save(item1);
        Item createItem2 = itemRepository.save(item2);
        Item createItem3 = itemRepository.save(item3);
        Item createItem4 = itemRepository.save(item4);
        Item createItem5 = itemRepository.save(item5);

        //when
        List<Item> itemList = itemRepository.findAll();

        //then

        Assertions.assertNotNull(itemList.get(0));
        Assertions.assertNotNull(itemList.get(1));
        Assertions.assertNotNull(itemList.get(2));
        Assertions.assertNotNull(itemList.get(3));
        Assertions.assertNotNull(itemList.get(4));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            itemList.get(5);
        });
        Assertions.assertNotNull(itemList.size() == 5);
        Assertions.assertEquals(createItem1.getId(), itemList.get(0).getId());
        Assertions.assertEquals(createItem2.getId(), itemList.get(1).getId());
        Assertions.assertEquals(createItem3.getId(), itemList.get(2).getId());
        Assertions.assertEquals(createItem4.getId(), itemList.get(3).getId());
        Assertions.assertEquals(createItem5.getId(), itemList.get(4).getId());
    }

    @Test
    @DisplayName("아이템 조회 - 조회 실패 시 ItemNotFoundException 던지기")
    @Transactional
    public void findItemByIdNotFoundTest() {
        //given
        Long invalidItemId = 782L;

        //expected
        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemRepository.findById(invalidItemId).orElseThrow(ItemNotFoundException::new);
        });
    }

    @Test
    @DisplayName("카테고리별 가장 최근에 등록된 아이템 조회")
    @Transactional
    public void getItemListByCategoryTest() throws Exception {
        //given
        String[] categories = {"top", "bottom", "hat", "acc", "shoes", "outer", "bag"};
        String[] colors = {"black", "gray", "white", "navy"};
        String[] sizes = {"small", "medium", "large", "xlarge"};
        String[] brands = {"nike", "adidas", "new balance", "champion", "yale"};
        int[] prices = {10000, 15000, 20000, 25000, 30000};


        Random random = new Random();
        Map<String, Integer> itemCountMap = new HashMap<>();
        int MAX_ITEMS_PER_CATEGORY = 20;

        for (int i = 0; i < 128; i++) {
            String category;

            do {
                category = categories[random.nextInt(categories.length)];
            } while (itemCountMap.getOrDefault(category, 0) >= MAX_ITEMS_PER_CATEGORY);

            itemCountMap.put(category, itemCountMap.getOrDefault(category, 0) + 1);

            String color = colors[random.nextInt(colors.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String brand = brands[random.nextInt(brands.length)];
            int price = prices[random.nextInt(prices.length)];

            Item item = Item.builder()
                    .itemName("아이템" + i)
                    .itemInfo("아이템 정보" + i)
                    .price(price)
                    .category(category)
                    .color(color)
                    .size(size)
                    .brand(brand)
                    .build();

            item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
            item.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");

            itemRepository.save(item);
        }

        Pageable pageable = PageRequest.of(1, 10);

        //when
        Page<Object[]> itemList = itemRepository.findRecentItemListByCategory(pageable, "top");
        itemList.getContent().forEach(items -> log.info(Arrays.toString(items)));

        List<Object[]> result = itemList.getContent();

        //then
        Assertions.assertFalse(itemList.isEmpty());
        Assertions.assertTrue(itemList.getSize() == 10);

        // 내림차순 검증
        LocalDateTime previousRegDate = null;
        for (Object[] objects : result) {
            Item item = (Item) objects[0];
            if (previousRegDate != null) {
                Assertions.assertTrue(item.getRegDate().isBefore(previousRegDate) || item.getRegDate().isEqual(previousRegDate));
            }
            previousRegDate = item.getRegDate();
        }

    }

    @Test
    @DisplayName("가장 최근에 등록된 아이템 조회")
    @Transactional
    public void findRecentItemListTest() throws Exception {
        //given
        String[] categories = {"top", "bottom", "hat", "acc", "shoes", "outer", "bag"};
        String[] colors = {"black", "gray", "white", "navy"};
        String[] sizes = {"small", "medium", "large", "xlarge"};
        String[] brands = {"nike", "adidas", "new balance", "champion", "yale"};
        int[] prices = {10000, 15000, 20000, 25000, 30000};


        Random random = new Random();
        Map<String, Integer> itemCountMap = new HashMap<>();
        int MAX_ITEMS_PER_CATEGORY = 20;

        for (int i = 0; i < 128; i++) {
            String category;

            do {
                category = categories[random.nextInt(categories.length)];
            } while (itemCountMap.getOrDefault(category, 0) >= MAX_ITEMS_PER_CATEGORY);

            itemCountMap.put(category, itemCountMap.getOrDefault(category, 0) + 1);

            String color = colors[random.nextInt(colors.length)];
            String size = sizes[random.nextInt(sizes.length)];
            String brand = brands[random.nextInt(brands.length)];
            int price = prices[random.nextInt(prices.length)];

            Item item = Item.builder()
                    .itemName("아이템" + i)
                    .itemInfo("아이템 정보" + i)
                    .price(price)
                    .category(category)
                    .color(color)
                    .size(size)
                    .brand(brand)
                    .build();

            item.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
            item.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");

            itemRepository.save(item);
        }

        Pageable pageable = PageRequest.of(0, 12);

        //when
        List<Object[]> result = itemRepository.findRecentItemsWithImages(pageable);
        List<ItemDto> dtoList = result.stream().map(res -> {
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

        //then
        Assertions.assertFalse(dtoList.isEmpty());
        Assertions.assertTrue(dtoList.size() == 12);

        // 내림차순 검증
        LocalDateTime previousRegDate = null;
        for (Object[] objects : result) {
            Item item = (Item) objects[0];
            if (previousRegDate != null) {
                Assertions.assertTrue(item.getRegDate().isBefore(previousRegDate) || item.getRegDate().isEqual(previousRegDate));
            }
            previousRegDate = item.getRegDate();
        }

    }

    @Test
    @DisplayName("아이템 수정")
    @Transactional
    public void updateItemTest() throws Exception {
        //given
        Item item1 = Item.builder()
                .itemName("아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("top")
                .color("navy")
                .size("small")
                .brand("nike")
                .build();
        item1.addFileName(UUID.randomUUID().toString() + "_" + "aaa.jpg");
        item1.addFileName(UUID.randomUUID().toString() + "_" + "bbb.jpg");

        Item createItem = itemRepository.save(item1);

        // 객체 복제
        Item originalItem = Item.builder()
                .id(createItem.getId())
                .itemName(createItem.getItemName())
                .itemInfo(createItem.getItemInfo())
                .price(createItem.getPrice())
                .category(createItem.getCategory())
                .color(createItem.getColor())
                .size(createItem.getSize())
                .brand(createItem.getBrand())
                .imageList(new ArrayList<>(createItem.getImageList()))
                .build();

        Item item2 = Item.builder()
                .id(createItem.getId())
                .itemName("아이템 수정")
                .itemInfo("아이템 정보 수정")
                .price(15000)
                .category("bottom")
                .color("green")
                .size("large")
                .brand("adidas")
                .build();
        item2.addFileName(UUID.randomUUID().toString() + "_" + "ccc.jpg");

        //when
        Item savedItem = itemRepository.save(item2);

        //then
        Item updateItem = itemRepository.findByIdWithImages(savedItem.getId()).orElseThrow(ItemNotFoundException::new);

        Assertions.assertNotNull(updateItem);
        Assertions.assertEquals(createItem.getId(), updateItem.getId());
        Assertions.assertNotEquals(originalItem.getItemName(), updateItem.getItemName());
        Assertions.assertNotEquals(originalItem.getItemInfo(), updateItem.getItemInfo());
        Assertions.assertNotEquals(originalItem.getPrice(), updateItem.getPrice());
        Assertions.assertNotEquals(originalItem.getColor(), updateItem.getColor());
        Assertions.assertNotEquals(originalItem.getBrand(), updateItem.getBrand());
        Assertions.assertNotEquals(originalItem.getCategory(), updateItem.getCategory());
        Assertions.assertNotEquals(originalItem.getSize(), updateItem.getSize());
        // 이미지 리스트 검증
        Assertions.assertEquals(1, updateItem.getImageList().size());
    }

}
