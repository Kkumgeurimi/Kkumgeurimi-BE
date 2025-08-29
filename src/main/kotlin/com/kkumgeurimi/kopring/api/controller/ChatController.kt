package com.kkumgeurimi.kopring.api.controller

import com.kkumgeurimi.kopring.domain.chat.service.ChatService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService
)
