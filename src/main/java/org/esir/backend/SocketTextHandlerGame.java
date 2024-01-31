package org.esir.backend;

import org.esir.backend.IO.JSONFormat;
import org.esir.backend.IO.decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketTextHandlerGame extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);

    decoder _decoder;

    public SocketTextHandlerGame() {
        _decoder = new decoder(new JSONFormat("default"));
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        String response = "";

        _decoder.setMessage(payload);

        _decoder.run();

        if(_decoder.getPackets() != null){
            response = "OK";
        }
        else {
            response = "KO";
        }

        try {
            session.sendMessage(new TextMessage(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
