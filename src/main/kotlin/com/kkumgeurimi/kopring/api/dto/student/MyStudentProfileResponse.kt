package com.kkumgeurimi.kopring.api.dto.student

import com.kkumgeurimi.kopring.domain.student.entity.Student

data class MyStudentProfileResponse (
    val name: String,
    val imageUrl: String?,
    val email: String
) {
    companion object {
        fun from(student: Student): MyStudentProfileResponse {
            return MyStudentProfileResponse(
                name = student.name,
                email = student.email,
                imageUrl = student.imageUrl
            )
        }
    }
}