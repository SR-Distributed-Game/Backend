package org.esir.backend.SocketHandler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketTextHandlerGame extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(SocketTextHandlerGame.class);

    private final ConcurrentHashMap<String, WebSocketSession> sessions;

    public SocketTextHandlerGame(ConcurrentHashMap<String, WebSocketSession> sessions) {
        this.sessions = sessions;
    }
    private int numthreads = 1;
    ExecutorService executorService = Executors.newFixedThreadPool(numthreads);

    private volatile boolean running = true;

    @PostConstruct
    public void init() {
        Thread loopThread = new Thread(this::runLoop);
        loopThread.start();
    }

    @PreDestroy
    public void destroy() {
        running = false;
    }

    private void runLoop() {
        while (running) {
            sendMessage();
        }
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

    public void sendMessage() {
        if (!QueueMaster.getInstance().get_queueEncoderOut().isEmpty()) {
            if (QueueMaster.getInstance().get_queueEncoderOut().size() >= 20) {
                logger.warn("SocketTextHandlerGame: queueEncoderOut is growing too fast");
                logger.warn("SocketTextHandlerGame: queueEncoderOut size: " + QueueMaster.getInstance().get_queueEncoderOut().size());
            }

            // Cas spécial pour numthreads == 1
            if (numthreads == 1) {
                String message = QueueMaster.getInstance().get_queueEncoderOut().poll();
                if (message != null) {
                    sendMessageToAllSessions(message);
                }
            } else {
                List<String> payload = new ArrayList<>();
                for (int i = 0; i < numthreads; i++) {
                    if (!QueueMaster.getInstance().get_queueEncoderOut().isEmpty()) {
                        String message = QueueMaster.getInstance().get_queueEncoderOut().poll();
                        if (message != null) payload.add(message);
                        else i--;
                    } else break;
                }

                for (int i = 0; i < payload.size(); i++) {
                    final int idThread = i;
                    Thread thread = new Thread(() -> sendMessageToAllSessions(payload.get(idThread)));
                    executorService.execute(thread);
                }
            }
        }
    }


    public void sendMessageToAllSessions(String payload) {
        sessions.values().forEach(session -> sendMessageToSession(session, payload));
    }


    public void sendMessageToSession(WebSocketSession session, String payload) {
        synchronized (session) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(payload));
                } catch (IOException e) {
                    logger.error("Error sending message", e);
                }
            }
        }
    }
}
