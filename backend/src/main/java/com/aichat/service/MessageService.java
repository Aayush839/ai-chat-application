package com.aichat.service;

import com.aichat.model.ChatMessage;
import com.aichat.model.Conversation;
import com.aichat.model.Message;
import com.aichat.model.User;
import com.aichat.repository.ConversationRepository;
import com.aichat.repository.MessageRepository;
import com.aichat.utility.UtilityClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public ConversationRepository conversationRepository;

    @Autowired
    public UserService userService;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveUserMessage(String conversationId, ChatMessage message, Principal principal) {
        Message message_entity=new Message();
        Conversation conversion = null;
        if(conversationId!=null){
            conversion = conversationRepository.findById(UtilityClasses.getLong(conversationId))
                    .orElseThrow(()->new RuntimeException("Conversation not found"));
        }
        String email = principal.getName();  // from JWT
//        Optional<User> user = userService.findUserIdByEmail(email);
        User user = userService.findUserIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        message_entity.setConversation(conversion);
        message_entity.setTimestamp(LocalDateTime.now());
        message_entity.setContent(message.getContent());
        message_entity.setSenderId(user.getId());
        message_entity.setType("USER");
        return messageRepository.save(message_entity);
    }
    public Message saveAiReply(String conversationId, String aiReply) {

        Conversation conversation = conversationRepository.findById(
                UtilityClasses.getLong(conversationId)
        ).orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message aiMessage = new Message();
        aiMessage.setConversation(conversation);
        aiMessage.setTimestamp(LocalDateTime.now());
        aiMessage.setContent(aiReply);
        aiMessage.setSenderId(-1L);
        aiMessage.setType("AI");

        return messageRepository.save(aiMessage);
    }
    public List<Message> getMessagesPaginated(Long conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findByConversation_IdOrderByTimestampDesc(conversationId, pageable);
    }
}
