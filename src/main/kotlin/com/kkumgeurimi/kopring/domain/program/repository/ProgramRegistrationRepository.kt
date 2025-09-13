package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import com.kkumgeurimi.kopring.domain.program.entity.ProgramRegistration
import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.student.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ProgramRegistrationRepository : JpaRepository<ProgramRegistration, Long> {
    fun findByProgramAndStudent(program: Program, student: Student): ProgramRegistration?
    fun countByProgram(program: Program): Long
    fun existsByProgramAndStudent(program: Program?, it: Student): Boolean

    // 특정 상태의 프로그램 목록 조회
    @Query("""
        SELECT pr FROM ProgramRegistration pr 
        JOIN FETCH pr.program p 
        WHERE pr.student = :student 
        AND pr.registrationStatus = :status
        ORDER BY pr.createdAt DESC
    """)
    fun findByStudentAndStatusWithProgramOrderByCreatedAtDesc(@Param("student") student: Student, @Param("status") status: RegistrationStatus): List<ProgramRegistration>

    @Query("SELECT pr FROM ProgramRegistration pr JOIN FETCH pr.program WHERE pr.student = :student ORDER BY pr.createdAt DESC")
    fun findByStudentWithProgramOrderByCreatedAtDesc(student: Student): List<ProgramRegistration>

    // 현재 사용자가 작성한 리뷰 목록 조회 (리뷰 점수가 있는 것만)
    @Query("""
        SELECT pr FROM ProgramRegistration pr 
        JOIN FETCH pr.program p 
        WHERE pr.student = :student 
        AND pr.reviewScore IS NOT NULL
        ORDER BY pr.createdAt DESC
    """)
    fun findReviewsByStudent(@Param("student") student: Student): List<ProgramRegistration>

    // endDate가 지난 REGISTERED 상태의 프로그램들을 COMPLETED로 업데이트
    @Modifying
    @Query("""
        UPDATE ProgramRegistration pr 
        SET pr.registrationStatus = :newStatus 
        WHERE pr.registrationStatus = :currentStatus 
        AND pr.program.endDate < :currentDate
    """)
    fun updateRegistrationStatusByEndDate(
        @Param("currentStatus") currentStatus: RegistrationStatus,
        @Param("newStatus") newStatus: RegistrationStatus,
        @Param("currentDate") currentDate: LocalDate
    ): Int

    @Query("""
        SELECT pr FROM ProgramRegistration pr 
        JOIN FETCH pr.program p 
        WHERE pr.student = :student 
        AND p.startDate > :date 
        AND pr.registrationStatus = :registrationStatus 
        ORDER BY p.startDate ASC
    """)
    fun findByStudentAndProgramStartDateAfterAndRegistrationStatusWithProgram(
        @Param("student") student: Student,
        @Param("date") date: LocalDate,
        @Param("registrationStatus") registrationStatus: RegistrationStatus
    ): List<ProgramRegistration>
}