package com.example.demo.repository;

import com.example.demo.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END AS chatPartnerId, " +
            "CASE WHEN m.sender.id = :userId THEN r.username ELSE s.username END AS chatPartnerName, " +
            "(SELECT m2.content FROM Message m2 WHERE " +
            " (m2.sender.id = m.sender.id AND m2.receiver.id = m.receiver.id) OR " +
            " (m2.sender.id = m.receiver.id AND m2.receiver.id = m.sender.id) " +
            " ORDER BY m2.sentAt DESC LIMIT 1) AS lastMessage, " +
            "MAX(m.sentAt) AS lastMessageTime, " +
            "SUM(CASE WHEN m.receiver.id = :userId AND m.isRead = false THEN 1 ELSE 0 END) AS unreadCount " +
            "FROM Message m " +
            "LEFT JOIN User s ON s.id = m.sender.id " +
            "LEFT JOIN User r ON r.id = m.receiver.id " +
            "WHERE m.sender.id = :userId OR m.receiver.id = :userId " +
            "GROUP BY " +
            "m.sender.id, m.receiver.id, s.username, r.username, " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.id = :userId THEN r.username ELSE s.username END")
    List<Object[]> findChatList(@Param("userId") Long userId);
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.sentAt")
    List<Message> findMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :receiverId AND m.receiver.id = :senderId AND m.isRead = false")
    List<Message> findUnreadMessages(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE (m.sender.id = :userId AND m.receiver.id = :chatPartnerId) " +
            "OR (m.sender.id = :chatPartnerId AND m.receiver.id = :userId)")
    void deleteMessagesBetweenUsers(@Param("userId") Long userId, @Param("chatPartnerId") Long chatPartnerId);

}
