package com.aichat.security;

import com.aichat.model.ChatMessage;
import com.aichat.model.Message;
import com.aichat.service.AiService;
import com.aichat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AiAsyncService {

    @Autowired
    private AiService aiService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Async
//    public void processAI(String conversationId, Message userMessage) {
//
//        // 1. Send PROCESSING
//        ChatMessage processing = new ChatMessage();
//        processing.setContent("");
//        processing.setSender(-1L);
//        processing.setType("AI");
//        processing.setStatus("PROCESSING");
//
//        messagingTemplate.convertAndSend(
//                "/topic/conversations/" + conversationId,
//                processing
//        );
//
//        // 2. Call AI
//        String aiReply = aiService.getAIResponse(userMessage.getContent(), conversationId);
//
//        // 3. Save AI message
//        Message saved = messageService.saveAiReply(conversationId, aiReply);
//
//
//        // 4. Send AI response
//        messagingTemplate.convertAndSend(
//                "/topic/conversations/" + conversationId,
//                ChatMessage.from(saved)
//        );
//    }

    @Async
    public void processAI(String conversationId, Message userMessage) {

        try {
            // 1. Call AI
            String aiReply = aiService.getAIResponse(
                    userMessage.getContent(),
                    conversationId
            );

            // 2. Save AI message
            Message saved = messageService.saveAiReply(conversationId, aiReply);

            // 3. Convert to DTO
            ChatMessage response = ChatMessage.from(saved);
            response.setStatus("SENT");

            // 4. Broadcast AI response
            messagingTemplate.convertAndSend(
                    "/topic/conversations/" + conversationId,
                    response
            );

        } catch (Exception e) {
            System.err.println("AI Async Error: " + e.getMessage());
        }
    }
}

