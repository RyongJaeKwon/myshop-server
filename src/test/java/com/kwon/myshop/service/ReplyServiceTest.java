package com.kwon.myshop.service;

import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Reply;
import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.repository.ItemRepository;
import com.kwon.myshop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ReplyServiceTest {

    @Autowired private ReplyService replyService;
    @Autowired private ItemRepository itemRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("리뷰 등록 및 조회")
    @Transactional
    public void createReplyTest() {
        // given
        Item item = Item.builder()
                .itemName("테스트 아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("hat")
                .color("black")
                .size("XLarge")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);

        Address address = Address.builder()
                .postcode("12345")
                .basic_address("강원도")
                .detail_address("원주시")
                .build();

        Member member = Member.builder()
                .email("qwer@daum.net")
                .userId("qwert2")
                .password("QWE@qwer1")
                .name("홍길동")
                .phone("010-0000-1234")
                .address(address)
                .build();
        Member savedMember = memberRepository.save(member);

        ReplyDto replyDto = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("적극 추천합니다")
                .build();

        //when
        Long id = replyService.create(replyDto);

        // then
        ReplyDto savedReplyDto = replyService.get(id);

        Assertions.assertNotNull(replyDto);
        Assertions.assertEquals(replyDto.getItemId(), savedReplyDto.getItemId());
        Assertions.assertEquals(replyDto.getMemberId(), savedReplyDto.getMemberId());
        Assertions.assertEquals(replyDto.getContent(), savedReplyDto.getContent());
    }

    @Test
    @DisplayName("리뷰 수정")
    @Transactional
    public void updateReplyTest() {
        // given
        Item item = Item.builder()
                .itemName("테스트 아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("hat")
                .color("black")
                .size("XLarge")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);

        Address address = Address.builder()
                .postcode("12345")
                .basic_address("강원도")
                .detail_address("원주시")
                .build();

        Member member = Member.builder()
                .email("qwer@daum.net")
                .userId("qwert2")
                .password("QWE@qwer1")
                .name("홍길동")
                .phone("010-0000-1234")
                .address(address)
                .build();
        Member savedMember = memberRepository.save(member);

        Reply reply = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("핏이 최고네요!!")
                .build();

        Long id = replyService.create(replyService.entityToDto(reply));
        ReplyDto savedReplyDto = replyService.get(id);

        ReplyDto replyDto = ReplyDto.builder()
                .id(savedReplyDto.getId())
                .itemId(savedReplyDto.getItemId())
                .memberId(savedReplyDto.getMemberId())
                .content("편하게 입기좋아요")
                .build();

        //when
        ReplyDto updateReplyDto = replyService.update(replyDto);

        // then
        Assertions.assertNotNull(updateReplyDto);
        Assertions.assertEquals(replyDto.getItemId(), updateReplyDto.getItemId());
        Assertions.assertEquals(replyDto.getMemberId(), updateReplyDto.getMemberId());
        Assertions.assertEquals(replyDto.getContent(), updateReplyDto.getContent());
        Assertions.assertEquals(updateReplyDto.getContent(), "편하게 입기좋아요");
    }

    @Test
    @DisplayName("itemId로 리뷰 조회")
    @Transactional
    public void getReplyListByItemIdTest() {
        // given
        Item item = Item.builder()
                .itemName("테스트 아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("hat")
                .color("black")
                .size("XLarge")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);

        Address address = Address.builder()
                .postcode("12345")
                .basic_address("강원도")
                .detail_address("원주시")
                .build();

        Member member = Member.builder()
                .email("qwer@daum.net")
                .userId("qwert2")
                .password("QWE@qwer1")
                .name("홍길동")
                .phone("010-0000-1234")
                .address(address)
                .build();
        Member savedMember = memberRepository.save(member);

        ReplyDto replyDto1 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("적극 추천합니다")
                .build();
        replyService.create(replyDto1);

        ReplyDto replyDto2 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("하나 더 구매 할게요")
                .build();
        replyService.create(replyDto2);

        ReplyDto replyDto3 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("소재가 좋네요")
                .build();
        replyService.create(replyDto3);

        //when
        List<ReplyDto> replyList = replyService.getReplyList(savedItem.getId());

        // then
        Assertions.assertNotNull(replyList);
        Assertions.assertTrue(replyList.size() == 3);
        Assertions.assertEquals(replyDto3.getItemId(), replyList.get(0).getItemId());
        Assertions.assertEquals(replyDto3.getMemberId(), replyList.get(0).getMemberId());
        Assertions.assertEquals(replyDto3.getContent(), replyList.get(0).getContent());
        Assertions.assertEquals("소재가 좋네요", replyList.get(0).getContent());

        Assertions.assertEquals(replyDto2.getItemId(), replyList.get(1).getItemId());
        Assertions.assertEquals(replyDto2.getMemberId(), replyList.get(1).getMemberId());
        Assertions.assertEquals(replyDto2.getContent(), replyList.get(1).getContent());
        Assertions.assertEquals("하나 더 구매 할게요", replyList.get(1).getContent());

        Assertions.assertEquals(replyDto1.getItemId(), replyList.get(2).getItemId());
        Assertions.assertEquals(replyDto1.getMemberId(), replyList.get(2).getMemberId());
        Assertions.assertEquals(replyDto1.getContent(), replyList.get(2).getContent());
        Assertions.assertEquals("적극 추천합니다", replyList.get(2).getContent());
    }

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    public void deleteReplyTest() {
        // given
        Item item = Item.builder()
                .itemName("테스트 아이템")
                .itemInfo("아이템 정보")
                .price(20000)
                .category("hat")
                .color("black")
                .size("XLarge")
                .brand("nike")
                .build();
        Item savedItem = itemRepository.save(item);

        Address address = Address.builder()
                .postcode("12345")
                .basic_address("강원도")
                .detail_address("원주시")
                .build();

        Member member = Member.builder()
                .email("qwer@daum.net")
                .userId("qwert2")
                .password("QWE@qwer1")
                .name("홍길동")
                .phone("010-0000-1234")
                .address(address)
                .build();
        Member savedMember = memberRepository.save(member);

        ReplyDto replyDto1 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("적극 추천합니다")
                .build();
        replyService.create(replyDto1);

        ReplyDto replyDto2 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("하나 더 구매 할게요")
                .build();
        Long replyDto2ReplyId = replyService.create(replyDto2);

        ReplyDto replyDto3 = ReplyDto.builder()
                .itemId(savedItem.getId())
                .memberId(savedMember.getId())
                .content("소재가 좋네요")
                .build();
        replyService.create(replyDto3);

        //when
        replyService.delete(replyDto2ReplyId);

        // then
        List<ReplyDto> replyList = replyService.getReplyList(savedItem.getId());

        Assertions.assertNotNull(replyList);
        Assertions.assertTrue(replyList.size() == 2);
        Assertions.assertEquals(replyDto3.getItemId(), replyList.get(0).getItemId());
        Assertions.assertEquals(replyDto3.getMemberId(), replyList.get(0).getMemberId());
        Assertions.assertEquals(replyDto3.getContent(), replyList.get(0).getContent());
        Assertions.assertEquals("소재가 좋네요", replyList.get(0).getContent());

        Assertions.assertEquals(replyDto1.getItemId(), replyList.get(1).getItemId());
        Assertions.assertEquals(replyDto1.getMemberId(), replyList.get(1).getMemberId());
        Assertions.assertEquals(replyDto1.getContent(), replyList.get(1).getContent());
        Assertions.assertEquals("적극 추천합니다", replyList.get(1).getContent());
    }

}
