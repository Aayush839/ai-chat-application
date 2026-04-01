package com.aichat.service;

import com.aichat.model.Conversation;
import com.aichat.repository.ConversationRepository;
import com.aichat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConversationService {

    @Autowired
    public UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation createConversation(Long userId) {
        Optional<Conversation> existing =conversationRepository.findByUserId(userId);
        Conversation conversation=null;
        if(existing.isPresent()){
            return existing.get();
        }
        conversation=new Conversation();
        conversation.setUser(userRepository.findById(userId).get());
        conversation.setStatus("OPEN");
        conversation.setCreatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    public Conversation getOpenConversation(Long userId) {
        Conversation conversation = conversationRepository.findFirstByUserAndStatus(userId, "OPEN");
        return conversation;
    }

}
