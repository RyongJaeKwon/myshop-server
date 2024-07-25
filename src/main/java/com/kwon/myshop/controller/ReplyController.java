package com.kwon.myshop.controller;

import com.kwon.myshop.dto.ReplyDto;
import com.kwon.myshop.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/")
    public Map<String, String> create(@RequestBody ReplyDto replyDto) {
        replyService.create(replyDto);

        return Map.of("RESULT", "SUCCESS");
    }

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
        log.info("replyList: " + replyList);
        return replyList;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        replyService.delete(id);

        return Map.of("RESULT", "SUCCESS");
    }
}
