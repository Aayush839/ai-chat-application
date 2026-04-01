package com.aichat.model;

import lombok.Data;

@Data
public class ChatMessage {

    private Long conversationId;
    private Long sender;
    private String content;
    private String type;
    private String status;

    public static ChatMessage from(Message message) {
        ChatMessage chat = new ChatMessage();
        chat.setConversationId(message.getConversation().getId());
        chat.setSender(message.getSenderId());
        chat.setContent(message.getContent());
        chat.setType(message.getType());
        chat.setStatus("DELIVERED");
        return chat;
    }
}