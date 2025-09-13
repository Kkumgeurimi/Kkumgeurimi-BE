package com.kkumgeurimi.kopring.domain.program.repository

import com.kkumgeurimi.kopring.domain.common.CostType
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
            AND (:programType IS NULL OR p.programType = :programType)
            AND (:costType IS NULL OR p.costType = :costType)
            AND (:targetAudience IS NULL OR p.targetAudience LIKE CONCAT('%', :targetAudience, '%'))
            AND (:keyword IS NULL OR (
                LOWER(p.programTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                OR LOWER(p.relatedMajor) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ))
            AND p.startDate >= :startDate
            AND p.endDate <= :endDate
        ORDER BY p.createdAt DESC
    """)
    fun findProgramsByFiltersOrderByLatest(
        @Param("interestCategory") interestCategory: Int?,
        @Param("programType") programType: Int?,
        @Param("costType") costType: CostType?,
        @Param("targetAudience") targetAudience: String?,
        @Param("keyword") keyword: String?,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<Program>

    // 인기순
    @Query("""
        SELECT p FROM Program p
        LEFT JOIN p.programLikes pl
        WHERE (:interestCategory IS NULL OR p.interestCategory = :interestCategory)
            AND (:programType IS NULL OR p.programType = :programType)
            AND (:costType IS NULL OR p.costType = :costType)
            AND (:targetAudience IS NULL OR p.targetAudience LIKE CONCAT('%', :targetAudience, '%'))
            AND (:keyword IS NULL OR (
                LOWER(p.programTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                OR LOWER(p.relatedMajor) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ))
            AND p.startDate >= :startDate
            AND p.endDate <= :endDate
        GROUP BY p
        ORDER BY COUNT(pl) DESC
    """)
    fun findProgramsByFiltersOrderByPopular(
        @Param("interestCategory") interestCategory: Int?,
        @Param("programType") programType: Int?,
        @Param("costType") costType: CostType?,
        @Param("targetAudience") targetAudience: String?,
        @Param("keyword") keyword: String?,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<Program>

    // 마감 임박순
    @Query("""
        SELECT p FROM Program p
        WHERE (:interestCategory IS NULL OR p.interestCategory = :interestCategory)
            AND (:programType IS NULL OR p.programType = :programType)
            AND (:costType IS NULL OR p.costType = :costType)
            AND (:targetAudience IS NULL OR p.targetAudience LIKE CONCAT('%', :targetAudience, '%'))
             AND (:keyword IS NULL OR (
                LOWER(p.programTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                OR LOWER(p.relatedMajor) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ))
            AND p.startDate >= :startDate
            AND p.endDate <= :endDate
        ORDER BY p.endDate ASC
    """)
    fun findProgramsByFiltersOrderByDeadline(
        @Param("interestCategory") interestCategory: Int?,
        @Param("programType") programType: Int?,
        @Param("costType") costType: CostType?,
        @Param("targetAudience") targetAudience: String?,
        @Param("keyword") keyword: String?,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<Program>

    @Query("""
        SELECT p
        FROM Program p
        LEFT JOIN p.programLikes pl
        WHERE pl.createdAt >= :oneWeekAgo
        GROUP BY p.programId
        ORDER BY COUNT(pl) DESC
    """)
    fun findTop4ProgramsByOrderByProgramLikesInLastWeek(@Param("oneWeekAgo") oneWeekAgo: LocalDate): List<Program>
}
