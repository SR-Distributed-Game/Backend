package org.esir.backend.SocketHandler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketTextHandlerEcho extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            session.sendMessage(new TextMessage(payload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
