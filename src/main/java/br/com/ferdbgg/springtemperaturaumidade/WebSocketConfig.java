package br.com.ferdbgg.springtemperaturaumidade;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Canal onde o Flutter vai "ouvir" as atualizações
        config.enableSimpleBroker("/topic");
        // Prefixo para mensagens enviadas do cliente para o servidor (se houver)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Ponto de conexão inicial (Handshake)
        registry.addEndpoint("/ws-sensor").setAllowedOrigins("*");
    }
    
}
