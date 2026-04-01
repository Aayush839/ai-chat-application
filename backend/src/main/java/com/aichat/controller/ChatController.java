package com.aichat.controller;

import com.aichat.model.ChatMessage;
import com.aichat.model.Conversation;
import com.aichat.model.Message;
import com.aichat.model.User;
import com.aichat.repository.ConversationRepository;
import com.aichat.security.AiAsyncService;
import com.aichat.service.AiService;
import com.aichat.service.ConversationService;
import com.aichat.service.MessageService;
import com.aichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/app")
public class ChatController {

    @Autowired
    public AiService aiService;

    @Autowired
    public MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private AiAsyncService aiAsyncService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

//    @MessageMapping("/sendMessage")
//    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

//    @MessageMapping("/chat/{conversationId}")
//    @SendTo("/topic/conversations/{conversationId}")
//    public ChatMessage sendMessage(@DestinationVariable String conversationId, ChatMessage message, Principal principal) {
//        // save message to DB via MessageService
//        System.out.println("Message is saved111");
//        messageService.saveMessageAndReturnUserMessage(conversationId, message,principal);
////        todo i should send aanother msg via ai or any user ,  not same msg.
//
//        return message;
//    }
//
//    @MessageMapping("/chat/{conversationId}")
//    @SendTo("/topic/conversations/{conversationId}")
//    public ChatMessage sendMessage(@DestinationVariable String conversationId,ChatMessage message, Principal principal) {
//
//        // 1. Save USER message
//        Message savedMessage = messageService.saveUserMessage(conversationId, message, principal);
//
//        messagingTemplate.convertAndSend(
//                "/topic/conversations/" + conversationId,
//                message
//        );
//
//        // 2. Call async AI (NEW)
//        aiAsyncService.processAI(conversationId, savedMessage);
//
//        // 3. Return USER message immediately
//        ChatMessage response = ChatMessage.from(savedMessage);
//        response.setStatus("PROCESSING"); // ✅ add this
//
//        return response;
//        }

    @MessageMapping("/chat")
    public ChatMessage sendMessage(ChatMessage message, Principal principal) {

        String email = principal.getName();

        User user = userService.findUserIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Get or create user's conversation
        Conversation convo = conversationService.createConversation(user.getId());

        // Save message
        Message savedMessage = messageService.saveUserMessage(
                String.valueOf(convo.getId()), message, principal
        );

        // Send to that specific conversation
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + convo.getId(),
                message
        );

        // AI response
        aiAsyncService.processAI(String.valueOf(convo.getId()), savedMessage);

        return ChatMessage.from(savedMessage);
    }
    @GetMapping("/messages/{conversationId}")
    public List<ChatMessage> getMessages(
            @PathVariable String conversationId,
            @RequestParam int page,
            @RequestParam int size) {

        List<Message> messages = messageService.getMessagesPaginated(Long.parseLong(conversationId),page,size);
        return messages.stream().map(ChatMessage::from).toList();
    }

}
