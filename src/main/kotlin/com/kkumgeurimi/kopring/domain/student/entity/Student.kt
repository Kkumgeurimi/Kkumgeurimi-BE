package com.kkumgeurimi.kopring.domain.student.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kkumgeurimi.kopring.domain.common.BaseTime
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "student")
class Student(
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    @Column(name = "email", unique = true, nullable = false, length = 255)
    val email: String,
    
    @field:NotBlank(message = "이름은 필수입니다")
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    
    @Column(name = "phone", length = 20)
    var phone: String? = null,
    
    @Column(name = "address", length = 500)
    var address: String? = null,
    
    @Column(name = "image_url", length = 1000)
    var imageUrl: String? = null,
    
    @Column(name = "birth", length = 10)
    var birth: String? = null,
    
    @Column(name = "school", length = 100)
    var school: String? = null,
    
    @Column(name = "interest_category", length = 100)
    var interestCategory: String? = null,
    
    @Column(name = "career", columnDefinition = "TEXT")
    var career: String? = null,
    
    @JsonIgnore
    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    @Column(name = "password", nullable = false)
    var password: String
) : BaseTime()
