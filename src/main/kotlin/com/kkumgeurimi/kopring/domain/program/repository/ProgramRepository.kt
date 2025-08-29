package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.program.entity.Program
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProgramRepository : JpaRepository<Program, Long> {

    fun findByProgramId(programId: String): Program?

    @Query("""
        SELECT p FROM Program p 
        WHERE (:interestCategory = 'all' OR p.interestCategory = :interestCategory)
        AND (:programType = 'all' OR p.programType = :programType)
        AND (:cost = 'all' OR 
            (:cost = 'free' AND (p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL)) OR
            (:cost = 'paid' AND p.price IS NOT NULL AND p.price != '무료' AND p.price != '0원' AND p.price != 'free'))
        AND (:startDate IS NULL OR p.startDate >= :startDate)
        AND (:endDate IS NULL OR p.endDate <= :endDate)
    """)
    fun findProgramsByFilters(
        @Param("interestCategory") interestCategory: String,
        @Param("programType") programType: String,
        @Param("cost") cost: String,
        @Param("startDate") startDate: String?,
        @Param("endDate") endDate: String?,
        pageable: Pageable
    ): Page<Program>

    @Query("""
        SELECT p FROM Program p 
        WHERE (:interestCategory = 'all' OR p.interestCategory = :interestCategory)
        AND (:programType = 'all' OR p.programType = :programType)
        AND (:cost = 'all' OR 
            (:cost = 'free' AND (p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL)) OR
            (:cost = 'paid' AND p.price IS NOT NULL AND p.price != '무료' AND p.price != '0원' AND p.price != 'free'))
        AND (:startDate IS NULL OR p.startDate >= :startDate)
        AND (:endDate IS NULL OR p.endDate <= :endDate)
        ORDER BY p.createdAt DESC
    """)
    fun findProgramsByFiltersOrderByLatest(
        @Param("interestCategory") interestCategory: String,
        @Param("programType") programType: String,
        @Param("cost") cost: String,
        @Param("startDate") startDate: String?,
        @Param("endDate") endDate: String?,
        pageable: Pageable
    ): Page<Program>

    @Query("""
        SELECT p FROM Program p 
        LEFT JOIN p.programLikes pl
        WHERE (:interestCategory = 'all' OR p.interestCategory = :interestCategory)
        AND (:programType = 'all' OR p.programType = :programType)
        AND (:cost = 'all' OR 
            (:cost = 'free' AND (p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL)) OR
            (:cost = 'paid' AND p.price IS NOT NULL AND p.price != '무료' AND p.price != '0원' AND p.price != 'free'))
        AND (:startDate IS NULL OR p.startDate >= :startDate)
        AND (:endDate IS NULL OR p.endDate <= :endDate)
        GROUP BY p
        ORDER BY COUNT(pl) DESC
    """)
    fun findProgramsByFiltersOrderByPopular(
        @Param("interestCategory") interestCategory: String,
        @Param("programType") programType: String,
        @Param("cost") cost: String,
        @Param("startDate") startDate: String?,
        @Param("endDate") endDate: String?,
        pageable: Pageable
    ): Page<Program>

    @Query("""
        SELECT p FROM Program p 
        WHERE (:interestCategory = 'all' OR p.interestCategory = :interestCategory)
        AND (:programType = 'all' OR p.programType = :programType)
        AND (:cost = 'all' OR 
            (:cost = 'free' AND (p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL)) OR
            (:cost = 'paid' AND p.price IS NOT NULL AND p.price != '무료' AND p.price != '0원' AND p.price != 'free'))
        AND (:startDate IS NULL OR p.startDate >= :startDate)
        AND (:endDate IS NULL OR p.endDate <= :endDate)
        ORDER BY p.endDate ASC
    """)
    fun findProgramsByFiltersOrderByDeadline(
        @Param("interestCategory") interestCategory: String,
        @Param("programType") programType: String,
        @Param("cost") cost: String,
        @Param("startDate") startDate: String?,
        @Param("endDate") endDate: String?,
        pageable: Pageable
    ): Page<Program>

    @Query("""
        SELECT p FROM Program p 
        WHERE (:interestCategory = 'all' OR p.interestCategory = :interestCategory)
        AND (:programType = 'all' OR p.programType = :programType)
        AND (:cost = 'all' OR 
            (:cost = 'free' AND (p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL)) OR
            (:cost = 'paid' AND p.price IS NOT NULL AND p.price != '무료' AND p.price != '0원' AND p.price != 'free'))
        AND (:startDate IS NULL OR p.startDate >= :startDate)
        AND (:endDate IS NULL OR p.endDate <= :endDate)
        ORDER BY 
            CASE 
                WHEN p.price = '무료' OR p.price = '0원' OR p.price = 'free' OR p.price IS NULL THEN 0
                ELSE 1
            END,
            p.createdAt DESC
    """)
    fun findProgramsByFiltersOrderByFree(
        @Param("interestCategory") interestCategory: String,
        @Param("programType") programType: String,
        @Param("cost") cost: String,
        @Param("startDate") startDate: String?,
        @Param("endDate") endDate: String?,
        pageable: Pageable
    ): Page<Program>
}