package com.aichat.controller;

import com.aichat.dto.LoginRequest;
import com.aichat.model.Conversation;
import com.aichat.model.User;
import com.aichat.repository.ConversationRepository;
import com.aichat.repository.UserRepository;
import com.aichat.security.JwtService;
import com.aichat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
//            return "Login successful";
            String token=jwtService.generateToken(user.getEmail());
            Map<String,String> response=new HashMap<>();
            response.put("token",token);
            response.put("email",user.getEmail());
            response.put("name",user.getName());
            System.out.println(response);
            return response;
        }

        return null;
//        return "Invalid credentials";
    }
//  for test workig of jwt via req-->jwt check has token then --> /test otherwise --> 403 error.
    @GetMapping("/test")
    public String testApi(){
        return "JWT Authentication working!";
    }

    @GetMapping("/profile/{email}")
    public User getUser(@PathVariable String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}