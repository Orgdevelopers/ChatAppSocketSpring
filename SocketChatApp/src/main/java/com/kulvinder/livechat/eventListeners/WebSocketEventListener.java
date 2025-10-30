package com.kulvinder.livechat.eventListeners;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.kulvinder.livechat.models.ChatMessage;

@Component
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.containsNativeHeader("username")) {
            System.out.println("A new user has connected :- " + accessor.getFirstNativeHeader("username"));
            messagingTemplate.convertAndSend("/allChat/messages",
                    ChatMessage.builder().from(accessor.getFirstNativeHeader("username"))
                            .text(accessor.getFirstNativeHeader("username") + " has joined the chat")
                            .broadcast(true).build());

        } else {
            System.out.println("A new user has connected :- no_username");
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            System.out.println("User disconnected: " + username);

            messagingTemplate.convertAndSend("/allChat/messages",
                    ChatMessage.builder().from(username)
                            .text(username + " has left the chat")
                            .broadcast(true)
                            .build());
        }
    }
}
