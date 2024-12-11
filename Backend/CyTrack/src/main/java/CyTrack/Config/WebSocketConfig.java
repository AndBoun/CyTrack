package CyTrack.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/{senderID}/{receiverID}")
                .setAllowedOriginPatterns("*") // Use allowedOriginPatterns instead of allowedOrigins
                .withSockJS();

        registry.addEndpoint("/conversations/{userID}")
                .setAllowedOriginPatterns("*") // Use allowedOriginPatterns instead of allowedOrigins
                .withSockJS();

        registry.addEndpoint("/leaderboard/{userID}")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/userSocket/{userID}")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/badgeSocket/{displayerID}/{viewerID}")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/workoutSocket/{displayerID}/{viewerID}")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/groupchat/{groupID}/{userID}")
                .setAllowedOriginPatterns("*")
                .withSockJS();

    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
