package com.kkumgeurimi.kopring.api.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "프로그램 검색 요청")
data class ProgramSearchRequest(
    @Schema(description = "관심 카테고리 (직무/전공) - 0-31", example = "0")
    val interestCategory: String? = null,
    
    @Schema(description = "체험유형", example = "field_company")
    val programType: String = "all",
    
    @Schema(description = "비용", example = "free")
    val cost: String = "all",
    
    @Schema(description = "시작 날짜", example = "2025-09-01")
    val startDate: String? = null,
    
    @Schema(description = "종료 날짜", example = "2025-12-31")
    val endDate: String? = null,
    
    @Schema(description = "정렬 기준", example = "latest", allowableValues = ["latest", "popular", "deadline"])
    val sortBy: String = "latest",
    
    @Schema(description = "페이지 번호", example = "1")
    val page: Int = 1,
    
    @Schema(description = "페이지 크기", example = "10")
    val size: Int = 10
) {
    fun getValidatedInterestCategory(): String {
        return if (interestCategory == null) {
            "all"
        } else {
            try {
                val code = interestCategory.toInt()
                if (code in 0..31) {
                    // 0-31 범위의 유효한 코드인지 확인
                    when (code) {
                        0 -> "HUM_SOC_RESEARCH"
                        1 -> "NAT_BIO_RESEARCH"
                        2 -> "ICT_RND_ENGINEERING"
                        3 -> "CONSTR_MINING_RND_ENGINEERING"
                        4 -> "MANUFACTURING_RND_ENGINEERING"
                        5 -> "WELFARE_RELIGION"
                        6 -> "EDUCATION"
                        7 -> "LAW"
                        8 -> "POLICE_FIRE_CORRECTION"
                        9 -> "MILITARY"
                        10 -> "HEALTH_MEDICAL"
                        11 -> "ARTS_DESIGN_MEDIA"
                        12 -> "SPORTS_RECREATION"
                        13 -> "SECURITY_GUARD"
                        14 -> "CARE_SERVICE"
                        15 -> "CLEANING_PERSONAL_SERVICE"
                        16 -> "BEAUTY_WEDDING_SERVICE"
                        17 -> "TRAVEL_LODGING_ENTERTAINMENT"
                        18 -> "FOOD_SERVICE"
                        19 -> "SALES"
                        20 -> "DRIVING_TRANSPORT"
                        21 -> "CONSTRUCTION_MINING"
                        22 -> "FOOD_PROCESSING_PRODUCTION"
                        23 -> "PRINT_WOOD_CRAFT_ETC_INSTALL_MAINT_PROD"
                        24 -> "MANUFACTURING_SIMPLE"
                        25 -> "MACHINE_INSTALL_MAINT_PROD"
                        26 -> "METAL_MATERIAL_INSTALL_MAINT_PROD"
                        27 -> "ELECTRICAL_ELECTRONIC_INSTALL_MAINT_PROD"
                        28 -> "ICT_INSTALL_MAINT"
                        29 -> "CHEM_ENV_INSTALL_MAINT_PROD"
                        30 -> "TEXTILE_APPAREL_PROD"
                        31 -> "AGRI_FISHERY"
                        else -> "all"
                    }
                } else {
                    "all"
                }
            } catch (e: NumberFormatException) {
                "all"
            }
        }
    }
}
