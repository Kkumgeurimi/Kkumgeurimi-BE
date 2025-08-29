package com.kkumgeurimi.kopring.domain.student.repository

import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Student?
    fun existsByEmail(email: String): Boolean
}
