package com.aichat.repository;

import com.aichat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation_Id(Long conversationId);
    List<Message> findByConversation_IdOrderByTimestampDesc(Long conversationId, Pageable pageable);
    List<Message> findTop10ByConversationIdOrderByTimestampAsc(Long conversationId);

}