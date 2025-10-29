package com.kulvinder.livechat.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.kulvinder.livechat.config.LottieAnimationConfig;
import com.kulvinder.livechat.models.ChatMessage;

@Controller  // ✅ Marks this class as a message-handling controller
public class ChatController {

    @MessageMapping("/sendMessage")
    // ✅ When the client sends message to /app/sendMessage, it comes here

    @SendTo("/allChat/messages")
    // ✅ The return value of this method will be broadcast to all clients subscribed to /topic/messages

    public ChatMessage broadcastMessage(ChatMessage message) {
        // ✅ This simply returns the same message that came from the client
        // In future, we can add logic like timestamp, filtering, formatting, etc.
        System.out.println("Received Message: " + message.getText() + " from " + message.getFrom());

        if (message.getText().contains("party_popper")) {
            message.setConfettie(true);
            message.setAnimation(LottieAnimationConfig.party_popper);

        }else{
            message.setConfettie(false);
        }
        return message;
    }
}
