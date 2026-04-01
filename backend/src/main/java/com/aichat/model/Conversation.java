package com.aichat.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne   // 🔥 IMPORTANT
    @JoinColumn(name = "user_id")
    private User user;

    private Long agentId;

    private String status;

    private LocalDateTime createdAt;
}