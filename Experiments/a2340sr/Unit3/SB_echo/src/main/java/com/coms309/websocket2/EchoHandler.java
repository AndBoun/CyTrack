package com.coms309.websocket2;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String requestPayload = message.getPayload();

        requestPayload = requestPayload + "Compute something!!!!! hey! they are numbers!.";
        session.sendMessage(new TextMessage("Echo: " + requestPayload));
    }
}
