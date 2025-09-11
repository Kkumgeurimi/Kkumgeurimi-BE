package com.kkumgeurimi.kopring.domain.program.entity

import com.kkumgeurimi.kopring.domain.common.BaseTime
import com.kkumgeurimi.kopring.domain.common.CostType
import com.kkumgeurimi.kopring.domain.common.VenueType
import jakarta.persistence.*
import org.hibernate.annotations.Check
import java.time.LocalDate

@Entity
@Check(constraints = "interest_category BETWEEN 0 AND 31")
@Table(name = "program")
class Program(
    @Id
    @Column(name = "program_id", unique = true, nullable = false)
    val programId: String,

    @Column(name = "program_title", length = 500, nullable = false)
    var programTitle: String,

    @Column(name = "provider", length = 255)
    var provider: String? = null,

    @Column(name = "target_audience", length = 500)
    var targetAudience: String? = null,

    @Column(name = "program_type", nullable = false)
    var programType: Int,

    @Column(name = "start_date", columnDefinition = "DATE")
    var startDate: LocalDate? = null,

    @Column(name = "end_date", columnDefinition = "DATE")
    var endDate: LocalDate? = null,

    @Column(name = "related_major", length = 255)
    var relatedMajor: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "cost_type", length = 20, nullable = false)
    val costType: CostType,

    @Column(name = "price", length = 100)
    val price: String? = null,

    @Column(name = "image_url", length = 1000)
    var imageUrl: String? = null,

    @Column(name = "eligible_region", length = 255)
    var eligibleRegion: String? = null,

    @Column(name = "venue_type", length = 255)
    var venueType: VenueType,

    @Column(name = "operate_cycle", length = 100)
    var operateCycle: String? = null,

    @Column(name = "interest_category")
    var interestCategory: Int? = null,

    @Column(name = "interest_text", length = 255)
    var interestText: String? = null
) : BaseTime() {

    @OneToOne(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    var programDetail: ProgramDetail? = null

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programLikes: MutableList<ProgramLike> = mutableListOf()

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRegistrations: MutableList<ProgramRegistration> = mutableListOf()

    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var programRecommendations: MutableList<ProgramRecommendation> = mutableListOf()
}