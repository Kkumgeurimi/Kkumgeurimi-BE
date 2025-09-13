package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ProgramRegistrationRepository : JpaRepository<ProgramRegistration, Long> {
    fun findByProgramAndStudent(program: Program, student: Student): ProgramRegistration?
    fun countByProgram(program: Program): Long
    fun findByStudentOrderByCreatedAtDesc(student: Student): List<ProgramRegistration>
    fun existsByProgramAndStudent(program: Program?, it: Student): Boolean
    
    // completed된 프로그램 목록 조회 (endDate가 현재 날짜보다 이전인 것들)
    @Query("""
        SELECT pr FROM ProgramRegistration pr 
        JOIN FETCH pr.program p 
        WHERE pr.student = :student 
        AND p.endDate < :currentDate 
        ORDER BY p.endDate DESC
    """)
    fun findCompletedProgramsByStudent(@Param("student") student: Student, @Param("currentDate") currentDate: LocalDate): List<ProgramRegistration>
    
    // 현재 사용자가 작성한 리뷰 목록 조회 (리뷰 점수가 있는 것만)
    @Query("""
        SELECT pr FROM ProgramRegistration pr 
        JOIN FETCH pr.program p 
        WHERE pr.student = :student 
        AND pr.reviewScore IS NOT NULL
        ORDER BY pr.createdAt DESC
    """)
    fun findReviewsByStudent(@Param("student") student: Student): List<ProgramRegistration>
}