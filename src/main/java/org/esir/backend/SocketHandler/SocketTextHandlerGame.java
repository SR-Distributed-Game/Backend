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

    private ConcurrentHashMap<String, WebSocketSession> sessions;

    public SocketTextHandlerGame(ConcurrentHashMap<String, WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        QueueMaster.getInstance().get_queueDecoderIn().add(payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("New session: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session.getId());
    }

    @Scheduled(fixedRateString = "${SocketTextHandlerGame.fixedRate}")
    public void sendMessage() {
        if (!QueueMaster.getInstance().get_queueEncoderOut().isEmpty()) {
            if (QueueMaster.getInstance().get_queueEncoderOut().size() >= 20) {
                logger.warn("SocketTextHandlerGame: queueEncoderOut is growing too fast");
                logger.warn("SocketTextHandlerGame: queueEncoderOut size: " + QueueMaster.getInstance().get_queueEncoderOut().size());
            }
            String payload = QueueMaster.getInstance().get_queueEncoderOut().poll();
            sendMessageToAllSessions(payload);
        }
    }

    @Async
    public void sendMessageToAllSessions(String payload) {
        sessions.values().forEach(session -> sendMessageToSession(session, payload));
    }


    public void sendMessageToSession(WebSocketSession session, String payload) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                logger.error("Error sending message", e);
            }
        }
    }
}
