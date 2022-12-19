package com.codelion.app_chat.controller;

import com.codelion.app_chat.ChatMessage;
import com.codelion.app_chat.RsData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WriteMessageRequest {
        private String authorName;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class WriteMessageResponse {
        private final long id;
    }

    /*작성*/
    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<WriteMessageResponse> writeMessage(@RequestBody WriteMessageRequest request) {
        ChatMessage message = new ChatMessage("윤서장", "안녕하세요.");

        chatMessages.add(message);

        return new RsData<>(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }

    /*조회*/
    @GetMapping("/messages")
    @ResponseBody
    public RsData<List<ChatMessage>> messages() {
        return new RsData<>(
                "S-1",
                "성공",
                chatMessages
        );
    }

    /*3번 게시물 이후 조회*/

}
