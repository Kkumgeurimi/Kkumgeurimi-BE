package com.kkumgeurimi.kopring.domain.chat.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.student.entity.Student
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    @Id
    @Column(name = "chat_message_id", unique = true, nullable = false)
    val chatMessageId: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,
    
    @Column(name = "content", columnDefinition = "TEXT")
    val content: String? = null,
    
    @Column(name = "message", columnDefinition = "TEXT")
    val message: String? = null,
    
    @Column(name = "role", length = 50)
    val role: String? = null
) : BaseTime()
