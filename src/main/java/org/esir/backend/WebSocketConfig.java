package org.esir.backend;

import org.esir.backend.IO.JSONFormat;
import org.esir.backend.IO.decoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        decoder decoder = new decoder(new JSONFormat());
        registry.addHandler(new SocketTextHandler(), "/echo").setAllowedOrigins("*");
    }
}
