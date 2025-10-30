package com.kulvinder.livechat.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.kulvinder.livechat.config.LottieAnimationConfig;
import com.kulvinder.livechat.models.ChatMessage;

@Controller 
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    public ChatController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getFrom());

        messagingTemplate.convertAndSend("/allChat/messages", chatMessage);
    }

    // @SendTo("/allChat/messages")


    public ChatMessage broadcastMessage(ChatMessage message) {
        System.out.println("Received Message: " + message.getText() + " from " + message.getFrom());

        if (message.getText().contains(":party_popper:")) {
            message.setConfettie(true);
            message.setAnimation(LottieAnimationConfig.party_popper);

        }
        return message;
    }
}
