package com.kkumgeurimi.kopring.domain.student.entity

import com.kkumgeurimi.kopring.api.dto.StudentResponse
import java.time.format.DateTimeFormatter

fun Student.toResponse(): StudentResponse {
    return StudentResponse(
        id = this.studentId,
        email = this.email,
        name = this.name,
        phone = this.phone,
        address = this.address,
        imageUrl = this.imageUrl,
        birth = this.birth,
        school = this.school,
        interestCategoryId = this.interestCategoryId,
        career = this.career,
        createdAt = this.createdAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        modifiedAt = this.modifiedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}
