package com.aichat.controller;

import com.aichat.model.Conversation;
import com.aichat.model.Message;
import com.aichat.model.User;
import com.aichat.service.ConversationService;
import com.aichat.service.MessageService;
import com.aichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

//import java.util.List;
//
//@RestController
//@RequestMapping("/api/conversations")
//public class ConversationController {
//
//    @Autowired
//    public ConversationService conversationService;
//    private final MessageService messageService;
//
//    public ConversationController(MessageService messageService) {
//        this.messageService = messageService;
//    }
//
////    @GetMapping("/{id}/messages")
////    public List<Message> getConversationMessages(@PathVariable Long id) {
////        return messageService.getMessagesByConversation(id);
////    }
//    // New method: create a conversation
//    @PostMapping
//    public Conversation createConversation(@RequestParam Long userId) {
//        return conversationService.createConversation(userId);
//    }
//
//    // Optional: get open conversation for user
//    @GetMapping("/open")
//    public Conversation getOpenConversation(@RequestParam Long userId) {
//        return conversationService.getOpenConversation(userId);
//    }
//}

@RestController
@RequestMapping("/api")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    @GetMapping("/conversation")
    public Conversation getConversation(Principal principal) {

        String email = principal.getName();

        User user = userService.findUserIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return conversationService.createConversation(user.getId()); // your method is OK
    }
}