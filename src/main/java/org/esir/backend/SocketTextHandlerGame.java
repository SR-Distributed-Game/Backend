package org.esir.backend;
import org.esir.backend.IO.JSONFormat;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketTextHandlerGame extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private final QueueMaster queueMaster = QueueMaster.getInstance();
    private final ConcurrentHashMap<String, WebSocketSession> sessions;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        logger.info("SocketTextHandlerGame: " + payload);
        queueMaster.get_queueDecoderIn().add(payload);
    }

    public SocketTextHandlerGame() {
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("size of sessions: " + sessions.size());
        sessions.put(session.getId(), session);
        logger.info("size of sessions: " + sessions.size());
        logger.info("New WebSocket session established with ID: " + session.getId());
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("size of sessions: " + sessions.size());
        sessions.remove(session.getId());
        logger.info("size of sessions: " + sessions.size());
        logger.info("WebSocket session closed with ID: " + session.getId());
    }

    @Scheduled(fixedRateString = "${SocketTextHandlerGame.fixedRate}")
    public void sendMessage() {
        if (!queueMaster.get_queueEncoderOut().isEmpty()){
            String payload = queueMaster.get_queueEncoderOut().poll();
            for (WebSocketSession session : sessions.values()) {
                if (session != null && session.isOpen()) {
                    try {
                        logger.info("SocketTextHandlerGame: sending message");
                        session.sendMessage(new TextMessage(payload));
                    } catch (IOException e) {
                        logger.error("Error sending message", e);
                    }
                }
            }
        }
    }
}
