package com.kkumgeurimi.kopring.domain.career.repository

import com.kkumgeurimi.kopring.domain.career.entity.CareerAnalysis
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface CareerAnalysisRepository : JpaRepository<CareerAnalysis, Long> {

    fun findTopByStudentOrderByCreatedAtDesc(student: Student): CareerAnalysis?
}
