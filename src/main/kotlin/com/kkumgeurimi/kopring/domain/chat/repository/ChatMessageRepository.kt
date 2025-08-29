package com.kkumgeurimi.kopring.domain.chat.repository

import com.kkumgeurimi.kopring.domain.chat.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long>
