package com.chatroom.chat;

/**
 * WebSocket message model
 */
class Message {
    String content;
    String sender;
    MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    void setContent(String content) {
        this.content = content;
    }

    void setSender(String sender) {
        this.sender = sender;
    }

    void setType(MessageType type) {
        this.type = type;
    }
}
