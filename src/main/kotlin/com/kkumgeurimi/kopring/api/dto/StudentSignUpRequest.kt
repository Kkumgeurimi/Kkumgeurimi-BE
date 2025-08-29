package com.kkumgeurimi.kopring.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class StudentSignUpRequest(
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,
    
    @field:NotBlank(message = "이름은 필수입니다")
    val name: String,
    
    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    val password: String,

    val passwordConfirm: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val imageUrl: String? = null,
    val birth: String? = null,
    val school: String? = null,
    val interestCategory: String? = null,
    val career: String? = null
)
