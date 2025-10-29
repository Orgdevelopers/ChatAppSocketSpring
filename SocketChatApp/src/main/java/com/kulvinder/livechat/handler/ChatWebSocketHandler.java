package com.kulvinder.livechat.handler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // add the new client session to the set
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // incoming message payload as String
        String payload = message.getPayload();

        // parse JSON to validate/inspect - expect something like {"from":"Alice","content":"Hi"}
        JsonNode node = mapper.readTree(payload);

        // optionally we can enrich or validate here; for simplicity just broadcast the original payload
        broadcast(payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // remove session when client disconnects
        sessions.remove(session);
    }

    private void broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);
        // iterate over a snapshot to avoid ConcurrentModification issues
        for (WebSocketSession sess : sessions) {
            try {
                if (sess.isOpen()) {
                    sess.sendMessage(textMessage);
                }
            } catch (IOException e) {
                // if sending fails, ignore for now (could log)
            }
        }
    }


    
}
