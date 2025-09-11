package com.kkumgeurimi.kopring.domain.student.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.program.entity.ProgramLike
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRecommendation
import com.kkumgeurimi.kopring.domain.chat.entity.ChatMessage
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "student",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    val studentId: Long = 0L,

    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    @Column(name = "email", nullable = false, length = 255)
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

    @Column(name = "interest_category")
    var interestCategory: Int? = null,

    @Column(name = "career", columnDefinition = "TEXT")
    var career: String? = null,

    @JsonIgnore
    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    @Column(name = "password", nullable = false)
    var password: String
) : BaseTime() {
    
    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programLikes: MutableList<ProgramLike> = mutableListOf()

    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRegistrations: MutableList<ProgramRegistration> = mutableListOf()

    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRecommendations: MutableList<ProgramRecommendation> = mutableListOf()

    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var chatMessages: MutableList<ChatMessage> = mutableListOf()
}
