package com.lancer.chat.controller;

import com.lancer.chat.dto.request.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
public class ChatController {

    @MessageMapping("/chat/send")
    @SendTo("/topic/chat/{projectId}")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(Instant.now());
        log.info("New message in project {}: {}", chatMessage.getProjectId(), chatMessage.getContent());
        return chatMessage;
    }

}