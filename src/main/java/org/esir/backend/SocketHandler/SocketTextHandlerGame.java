package org.esir.backend.SocketHandler;

import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
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

    private static final Logger logger = LoggerFactory.getLogger(SocketTextHandlerGame.class);
    private final QueueMaster queueMaster = QueueMaster.getInstance();

    private ConcurrentHashMap<String, WebSocketSession> sessions;

    public SocketTextHandlerGame(ConcurrentHashMap<String, WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        logger.info("SocketTextHandlerGame: " + payload);
        queueMaster.get_queueDecoderIn().add(payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put(session.getId(), session);
        logger.info("New WebSocket session established with ID: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
        logger.info("WebSocket session closed with ID: " + session.getId());
    }

    @Scheduled(fixedRateString = "${SocketTextHandlerGame.fixedRate}")
    public void sendMessage() {
        if (!queueMaster.get_queueEncoderOut().isEmpty()) {
            String payload = queueMaster.get_queueEncoderOut().poll();
            sessions.values().forEach(session -> sendMessageToSession(session, payload));
        }
    }

    @Async
    public void sendMessageToSession(WebSocketSession session, String payload) {
        if (session != null && session.isOpen()) {
            try {
                logger.info("SocketTextHandlerGame: sending message " +  payload);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                logger.error("Error sending message", e);
            }
        }
    }
}