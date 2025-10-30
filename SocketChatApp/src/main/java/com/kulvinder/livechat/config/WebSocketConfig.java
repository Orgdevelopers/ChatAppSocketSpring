package com.kulvinder.livechat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  //Enables WebSocket message handling with STOMP protocol
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint that clients will use to connect
        registry.addEndpoint("/chat-websocket") // URL for WebSocket handshake
                .setAllowedOriginPatterns("*")  // Allows clients from any domain (good for testing)
                .withSockJS();                  // Enables fallback for browsers without WebSocket support
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker to carry messages back to clients
        config.enableSimpleBroker("/allChat"); // Messages will be sent to destinations starting with /allChat
        config.setApplicationDestinationPrefixes("/app"); // Client messages must be prefixed with /app
    }

}
