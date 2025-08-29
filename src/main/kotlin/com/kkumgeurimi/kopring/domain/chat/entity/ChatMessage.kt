package com.kkumgeurimi.kopring.domain.chat.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    @Column(name = "chat_message_id", unique = true, nullable = false)
    val chatMessageId: String,
    
    @Column(name = "student_id", nullable = false)
    val studentId: Long,
    
    @Column(name = "content", columnDefinition = "TEXT")
    val content: String? = null,
    
    @Column(name = "message", columnDefinition = "TEXT")
    val message: String? = null,
    
    @Column(name = "role", length = 50)
    val role: String? = null
) : BaseTime()
