package com.kwon.myshop.controller;

import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/")
    public Map<String, Long> create(@RequestBody ReplyDto replyDto) {
        Long id = replyService.create(replyDto);

        return Map.of("RESULT", id);
    }

    @PreAuthorize("#replyDto.userId == principal.username")
    @PutMapping("/")
    public ReplyDto update(@RequestBody ReplyDto replyDto) {
        return replyService.update(replyDto);
    }

    @GetMapping("/{id}")
    public ReplyDto getReply(@PathVariable Long id) {
        return replyService.get(id);
    }

    @GetMapping("/item/{itemId}")
    public List<ReplyDto> getReplyList(@PathVariable Long itemId) {
        List<ReplyDto> replyList = replyService.getReplyList(itemId);

        return replyList;
    }

    @PreAuthorize("#userId == principal.username")
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id, @RequestParam String userId) {
        replyService.delete(id);

        return Map.of("RESULT", "SUCCESS");
    }
}
