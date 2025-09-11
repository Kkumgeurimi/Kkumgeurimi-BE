package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.common.ProgramType
import jakarta.persistence.*

@Entity
@Table(name = "program")
class Program(
    @Id
    @Column(name = "program_id", unique = true, nullable = false)
    val programId: String,

    @Column(name = "program_title", length = 500, nullable = false)
    val programTitle: String,

    @Column(name = "provider", length = 255)
    val provider: String? = null,

    @Column(name = "target_audience", length = 500)
    val targetAudience: String? = null,

    @Column(name = "program_type", nullable = false)
    @Enumerated(EnumType.STRING)   // DB에 enum 이름 그대로 저장
    val programType: ProgramType = ProgramType.ALL,

    @Column(name = "start_date", length = 100)
    val startDate: String? = null,

    @Column(name = "end_date", length = 100)
    val endDate: String? = null,

    @Column(name = "related_major", length = 255)
    val relatedMajor: String? = null,

    @Column(name = "price", length = 100)
    val price: String? = null,

    @Column(name = "image_url", length = 1000)
    val imageUrl: String? = null,

    @Column(name = "eligible_region", length = 255)
    val eligibleRegion: String? = null,

    @Column(name = "venue_region", length = 255)
    val venueRegion: String? = null,

    @Column(name = "operate_cycle", length = 100)
    val operateCycle: String? = null,

    @Column(name = "interest_category")
    val interestCategory: Int? = null,

    @Column(name = "interest_text", length = 255)
    val interestText: String? = null
) : BaseTime() {
    val isFree: Boolean
        get() = price?.contains("무료") == true

    val priceValue: Int?
        get() = if (isFree) null
        else price?.replace(",", "")?.replace("원", "")?.toIntOrNull()

    @OneToOne(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    var programDetail: ProgramDetail? = null

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programLikes: MutableList<ProgramLike> = mutableListOf()

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRegistrations: MutableList<ProgramRegistration> = mutableListOf()

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRecommendations: MutableList<ProgramRecommendation> = mutableListOf()
}