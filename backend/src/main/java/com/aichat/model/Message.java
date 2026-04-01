package com.aichat.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String type;

    private LocalDateTime timestamp;
}
