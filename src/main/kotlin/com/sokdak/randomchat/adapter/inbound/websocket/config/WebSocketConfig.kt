package com.sokdak.randomchat.adapter.inbound.websocket.config

import com.sokdak.randomchat.adapter.inbound.websocket.handlers.RandomChatWebSocketHandler
import com.sokdak.randomchat.adapter.inbound.websocket.interceptors.WebSocketAuthInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val randomChatWebSocketHandler: RandomChatWebSocketHandler,
    private val webSocketAuthInterceptor: WebSocketAuthInterceptor,
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(randomChatWebSocketHandler, "/ws/random-chat")
            .addInterceptors(webSocketAuthInterceptor)
            .setAllowedOriginPatterns("*")
    }
}
