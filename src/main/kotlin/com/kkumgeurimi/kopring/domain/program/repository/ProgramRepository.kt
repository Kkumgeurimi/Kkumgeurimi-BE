package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.InterestCategory
import com.kkumgeurimi.kopring.domain.common.ProgramType
import com.kkumgeurimi.kopring.domain.program.entity.Program
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ProgramRepository : JpaRepository<Program, Long> {

    // 상세 조회 (항상 Program + ProgramDetail 같이 로딩)
    @EntityGraph(attributePaths = ["programDetail"])
    fun findByProgramId(programId: String): Program?

    // 최신순
    @Query("""
        SELECT p FROM Program p
        WHERE (:interestCategory IS NULL OR p.interestCategory = :interestCategory)
          AND (:programType = 'ALL' OR p.programType = :programType)
          AND (:cost = 'ALL'
               OR (:cost = 'FREE' AND (p.price LIKE '%무료%' OR p.price = '0원' OR p.price = 'free'))
               OR (:cost = 'PAID' AND (p.price NOT LIKE '%무료%' AND p.price != '0원' AND p.price != 'free')))
          AND (:startDate IS NULL OR p.startDate >= :startDate)
          AND (:endDate IS NULL OR p.endDate <= :endDate)
        ORDER BY p.createdAt DESC
    """)
    fun findProgramsByFiltersOrderByLatest(
        @Param("interestCategory") interestCategory: InterestCategory?,
        @Param("programType") programType: ProgramType,
        @Param("cost") cost: CostType,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): Page<Program>

    // 인기순
    @Query("""
        SELECT p FROM Program p
        LEFT JOIN p.programLikes pl
        WHERE (:interestCategory IS NULL OR p.interestCategory = :interestCategory)
          AND (:programType = 'ALL' OR p.programType = :programType)
          AND (:cost = 'ALL'
               OR (:cost = 'FREE' AND (p.price LIKE '%무료%' OR p.price = '0원' OR p.price = 'free'))
               OR (:cost = 'PAID' AND (p.price NOT LIKE '%무료%' AND p.price != '0원' AND p.price != 'free')))
          AND (:startDate IS NULL OR p.startDate >= :startDate)
          AND (:endDate IS NULL OR p.endDate <= :endDate)
        GROUP BY p
        ORDER BY COUNT(pl) DESC
    """)
    fun findProgramsByFiltersOrderByPopular(
        @Param("interestCategory") interestCategory: InterestCategory?,
        @Param("programType") programType: ProgramType,
        @Param("cost") cost: CostType,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): Page<Program>

    // 마감 임박순
    @Query("""
        SELECT p FROM Program p
        WHERE (:interestCategory IS NULL OR p.interestCategory = :interestCategory)
          AND (:programType = 'ALL' OR p.programType = :programType)
          AND (:cost = 'ALL'
               OR (:cost = 'FREE' AND (p.price LIKE '%무료%' OR p.price = '0원' OR p.price = 'free'))
               OR (:cost = 'PAID' AND (p.price NOT LIKE '%무료%' AND p.price != '0원' AND p.price != 'free')))
          AND (:startDate IS NULL OR p.startDate >= :startDate)
          AND (:endDate IS NULL OR p.endDate <= :endDate)
        ORDER BY p.endDate ASC
    """)
    fun findProgramsByFiltersOrderByDeadline(
        @Param("interestCategory") interestCategory: InterestCategory?,
        @Param("programType") programType: ProgramType,
        @Param("cost") cost: CostType,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): Page<Program>
}