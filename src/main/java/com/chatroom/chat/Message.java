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

    void setContent(String content) {
        this.content = content;
    }

    void setSender(String sender) {
        this.sender = sender;
    }

    void setType(MessageType type) {
        this.type = type;
    }

    void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}
