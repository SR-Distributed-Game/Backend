package org.esir.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.TextMessage;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSocketTests {

    @LocalServerPort
    private int port;

    private WebSocketClient client;
    private WebSocketSession session;

    private final String WS_URI = "ws://localhost:%d/ws/message";

    @BeforeEach
    public void setup() {
        client = new StandardWebSocketClient();
    }

    @Test
    public void testWebSocket() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final TextMessage[] message = new TextMessage[1];

        session = client.doHandshake(new TextWebSocketHandler() {
            @Override
            public void handleTextMessage(WebSocketSession session, TextMessage receivedMessage) {
                message[0] = receivedMessage;
                latch.countDown();
            }
        }, new WebSocketHttpHeaders(), URI.create(String.format(WS_URI, port))).get();

        String msg = "Hello WebSocket";
        session.sendMessage(new TextMessage(msg));

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        session.close();

        assertTrue(message[0].getPayload().contains("Reçu ! " + msg));
    }
}
