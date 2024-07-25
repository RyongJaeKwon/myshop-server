package com.kwon.myshop.service;

import com.kwon.myshop.domain.Item;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Reply;
import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.exception.ItemNotFoundException;
import com.kwon.myshop.exception.MemberNotFoundException;
import com.kwon.myshop.exception.ReplyNotFoundException;
import com.kwon.myshop.repository.ItemRepository;
import com.kwon.myshop.repository.MemberRepository;
import com.kwon.myshop.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public void create(ReplyDto replyDto) {
        replyRepository.save(dtoToEntity(replyDto));
    }

    public ReplyDto update(ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyDto.getId()).orElseThrow(ReplyNotFoundException::new);
        reply.changeContent(replyDto.getContent());

        Reply savedReply = replyRepository.save(reply);

        return entityToDto(savedReply);
    }

    public ReplyDto get(Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);

        return entityToDto(reply);
    }

    public List<ReplyDto> getReplyList(Long itemId) {
        return replyRepository.getReplyListByItemId(itemId);
    }

    public void delete(Long id) {
        replyRepository.deleteById(id);
    }

    public Reply dtoToEntity(ReplyDto replyDto) {
        Item item = itemRepository.findById(replyDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        Member member = memberRepository.findById(replyDto.getMemberId()).orElseThrow(MemberNotFoundException::new);

        return Reply.builder()
                .member(member)
                .item(item)
                .content(replyDto.getContent())
                .build();
    }

    public ReplyDto entityToDto(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .memberId(reply.getMember().getId())
                .itemId(reply.getItem().getId())
                .userId(reply.getMember().getUserId())
                .content(reply.getContent())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }

}
