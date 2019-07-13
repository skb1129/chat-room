package com.chatroom.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message message) {
        message.setOnlineCount(onlineSessions.size());
        onlineSessions.forEach((s, session) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String JSON = mapper.writeValueAsString(message);
                session.getBasicRemote().sendText(JSON);
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineSessions.put(session.getId(), session);
        Message message = new Message();
        message.setContent(String.format("%s has joined the room", session.getId()));
        message.setSender(session.getId());
        message.setType(Message.MessageType.JOIN);
        sendMessageToAll(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = new Message();
        message.setContent(jsonStr);
        message.setSender(session.getId());
        message.setType(Message.MessageType.SPEAK);
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId());
        Message message = new Message();
        message.setContent(String.format("%s has left the room", session.getId()));
        message.setSender(session.getId());
        message.setType(Message.MessageType.LEAVE);
        sendMessageToAll(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
