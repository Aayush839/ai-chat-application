package com.aichat.repository;

import com.aichat.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

//    List<Conversation> findByCustomerId(Long customerId);

//    @Query("SELECT c FROM Conversation c WHERE c.customerId = :customerId AND c.status = :status")
//    Optional<Conversation> findActiveChat(@Param("customerId") Long customerId, @Param("status") String status);
    @Query(value = "Select c from Conversation c where c.user.id=:userId and c.status=:status")
    Conversation findFirstByUserAndStatus(@Param("userId") Long userId,@Param("status") String status);
    Optional<Conversation> findByUserId(Long id);
}