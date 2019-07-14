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
 * @see Session WebSocket Session
 */

@Component
@ServerEndpoint(value = "/chat", configurator = WebSocketConfig.class)
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * All User Names
     */
    private static final Map<String, String> userNames = new ConcurrentHashMap<>();

    /**
     * Send message to all users.
     * @param message: Message object to send
     */
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
     * Format user has joined message content
     * @param sender: Username
     * @return Formatted message content
     */
    private static String getJoinMessage(String sender) {
        return String.format("\"%s\" has joined the room", sender);
    }

    /**
     * Format user has left message content
     * @param sender: Username
     * @return Formatted message content
     */
    private static String getLeaveMessage(String sender) {
        return String.format("\"%s\" has left the room", sender);
    }

    /**
     * Open connection and add session.
     * @param session: WebSocket Session object
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineSessions.put(session.getId(), session);
        String username = (String) session.getUserProperties().get("username");
        userNames.put(session.getId(), username);
        Message message = new Message(getJoinMessage(username), username, Message.MessageType.JOIN);
        sendMessageToAll(message);
    }

    /**
     * Create message object and send message to all.
     * @param session: WebSocket Session object
     * @param content: Message content sent from client
     */
    @OnMessage
    public void onMessage(Session session, String content) {
        Message message = new Message(content, userNames.get(session.getId()), Message.MessageType.SPEAK);
        sendMessageToAll(message);
    }

    /**
     * Close connection and remove session.
     * @param session: WebSocket Session object
     */
    @OnClose
    public void onClose(Session session) {
        Message message = new Message(
                getLeaveMessage(userNames.get(session.getId())),
                userNames.get(session.getId()),
                Message.MessageType.LEAVE);
        onlineSessions.remove(session.getId());
        userNames.remove(session.getId());
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
