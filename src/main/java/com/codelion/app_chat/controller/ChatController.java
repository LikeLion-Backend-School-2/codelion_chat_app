package com.codelion.app_chat.controller;

import com.codelion.app_chat.ChatMessage;
import com.codelion.app_chat.RsData;
import com.codelion.app_chat.util.SseEmitters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SseEmitters sseEmitters;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }

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
        ChatMessage message = new ChatMessage(request.authorName, request.content);

        chatMessages.add(message);
        sseEmitters.noti("chat__messageAdded");

        return new RsData<>(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }

    /*조회*/
    /*3번 게시물 이후 조회*/
    @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(MessagesRequest request) {
        List<ChatMessage> messages = chatMessages;

        if (request.fromId != null) {
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == request.fromId)
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                messages = messages.subList(index + 1, messages.size());
            }
        }

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages, messages.size())
        );
    }

    @Getter
    @AllArgsConstructor
    public static class MessagesResponse {
        private final List<ChatMessage> messages;
        private final long count;
    }

    @Getter
    @AllArgsConstructor
    public static class MessagesRequest {
        private final Long fromId;
    }
}
