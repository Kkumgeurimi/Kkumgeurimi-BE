package com.kkumgeurimi.kopring.domain.chat.service

import com.kkumgeurimi.kopring.domain.chat.repository.ChatMessageRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatMessageRepository: ChatMessageRepository
)
