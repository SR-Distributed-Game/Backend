package org.esir.backend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public ConcurrentHashMap<String, WebSocketSession> sessions() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketTextHandlerEcho(), "/echo").setAllowedOrigins("*");
        registry.addHandler(new SocketTextHandlerGame(sessions()), "/game").setAllowedOrigins("*");
    }
}
