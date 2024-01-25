package org.esir.backend;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        System.out.println("Message reçu: " + payload);
        try {
            session.sendMessage(new TextMessage("Reçu ! " + payload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
