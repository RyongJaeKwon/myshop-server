package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Reply;
import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.exception.ReplyNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ReplyRepositoryTest {

    @Autowired ReplyRepository replyRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("리뷰 등록")
    @Transactional
    public void saveReplyTest() throws Exception {
        //given
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

        //when
        Reply savedReply = replyRepository.save(reply);
        
        //then
        Assertions.assertNotNull(savedReply);
        Assertions.assertEquals(savedReply.getItem().getId(), savedItem.getId());
        Assertions.assertEquals(savedReply.getMember().getId(), savedMember.getId());
        Assertions.assertEquals(savedReply.getContent(), reply.getContent());
    }

    @Test
    @DisplayName("리뷰 수정")
    @Transactional
    public void updateReplyTest() throws Exception {
        //given
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
        Reply savedReply = replyRepository.save(reply);

        //when
        savedReply.changeContent("정사이즈 핏 아주 예쁩니다");
        Reply updateReply = replyRepository.save(savedReply);

        //then
        Assertions.assertEquals(updateReply.getItem().getId(), savedItem.getId());
        Assertions.assertEquals(updateReply.getMember().getId(), savedMember.getId());
        Assertions.assertEquals(updateReply.getContent(), "정사이즈 핏 아주 예쁩니다");
    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    public void getReplyTest() throws Exception {
        //given
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
        Reply savedReply = replyRepository.save(reply);

        //when
        Reply getReply = replyRepository.findById(savedReply.getId()).orElseThrow(ReplyNotFoundException::new);

        //then
        Assertions.assertNotNull(getReply);
        Assertions.assertEquals(getReply.getId(), savedReply.getId());
        Assertions.assertEquals(getReply.getMember(), savedReply.getMember());
        Assertions.assertEquals(getReply.getItem(), savedReply.getItem());
        Assertions.assertEquals(getReply.getContent(), savedReply.getContent());
    }
    
    @Test
    @DisplayName("ItemId로 리뷰 조회")
    @Transactional
    public void getReplyListByItemIdTest() throws Exception {
        //given
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

        Reply reply1 = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("핏이 최고네요!!")
                .build();
        Reply savedReply1 = replyRepository.save(reply1);

        Reply reply2 = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("최고에요")
                .build();
        Reply savedReply2 = replyRepository.save(reply2);

        Reply reply3 = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("재구매 희망합니다")
                .build();
        Reply savedReply3 = replyRepository.save(reply3);

        //when
        List<ReplyDto> replyDtos = replyRepository.getReplyListByItemId(savedItem.getId());

        //then
        Assertions.assertNotNull(replyDtos);
        Assertions.assertTrue(replyDtos.size() == 3);
        Assertions.assertEquals(replyDtos.get(0).getContent(), savedReply3.getContent());
        Assertions.assertEquals(replyDtos.get(1).getContent(), savedReply2.getContent());
        Assertions.assertEquals(replyDtos.get(2).getContent(), savedReply1.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    public void deleteReplyTest() throws Exception {
        //given
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

        Reply reply1 = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("핏이 최고네요!!")
                .build();
        Reply savedReply1 = replyRepository.save(reply1);

        Reply reply2 = Reply.builder()
                .item(savedItem)
                .member(savedMember)
                .content("최고에요")
                .build();
        Reply savedReply2 = replyRepository.save(reply2);

        //when
        replyRepository.deleteById(savedReply1.getId());

        //then
        Optional<Reply> deleteReply = replyRepository.findById(savedReply1.getId());
        List<ReplyDto> replyDtos = replyRepository.getReplyListByItemId(savedItem.getId());

        Assertions.assertTrue(deleteReply.isEmpty());
        Assertions.assertTrue(replyDtos.size() == 1);
        Assertions.assertEquals(replyDtos.get(0).getMemberId(), savedReply2.getMember().getId());
        Assertions.assertEquals(replyDtos.get(0).getItemId(), savedReply2.getItem().getId());
        Assertions.assertEquals(replyDtos.get(0).getContent(), savedReply2.getContent());
    }
}
