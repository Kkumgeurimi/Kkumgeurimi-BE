package com.kkumgeurimi.kopring.api.dto

data class StudentResponse(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String?,
    val address: String?,
    val imageUrl: String?,
    val birth: String?,
    val school: String?,
    val interestCategory: Int?,
    val career: String?,
    val createdAt: String?,
    val modifiedAt: String?
)
