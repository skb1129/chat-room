package com.chatroom.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WebSocket message model
 */
class Message {
    @JsonProperty("msg")
    String content;

    @JsonProperty("username")
    String sender;

    @JsonProperty("type")
    MessageType type;

    @JsonProperty("onlineCount")
    int onlineCount;

    public enum MessageType {
        SPEAK, JOIN, LEAVE
    }

    Message(String content, String sender, MessageType type) {
        this.content = content;
        this.sender = sender;
        this.type = type;
    }

    void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}
