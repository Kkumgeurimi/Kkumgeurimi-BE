package com.kkumgeurimi.kopring.domain.student.service

import com.kkumgeurimi.kopring.domain.student.repository.StudentRepository
import org.springframework.stereotype.Component

@Component
class NicknameGenerator(
    private val studentRepository: StudentRepository
) {
    private val adjectives = listOf("꿈나무", "햇살", "별빛", "바람결", "몽실이")

    fun generateUnique(): String {
        var nickname: String
        do {
            nickname = "${adjectives.random()}${(1..9999).random()}"
        } while (studentRepository.existsByNickname(nickname))
        return nickname
    }
}
