package com.kkumgeurimi.kopring.domain.common

enum class InterestCategory(val code: Int, val label: String) {
    // 기존 라벨들 (주석처리)
    // HUM_SOC_RESEARCH(0, "인문·사회과학 연구직"),
    // NAT_BIO_RESEARCH(1, "자연·생명과학 연구직"),
    // ICT_RND_ENGINEERING(2, "정보통신 연구개발직 및 공학기술직"),
    // CONSTR_MINING_RND_ENGINEERING(3, "건설·채굴 연구개발직 및 공학기술직"),
    // MANUFACTURING_RND_ENGINEERING(4, "제조 연구개발직 및 공학기술직"),
    // WELFARE_RELIGION(5, "사회복지·종교직"),
    // EDUCATION(6, "교육직"),
    // LAW(7, "법률직"),
    // POLICE_FIRE_CORRECTION(8, "경찰·소방·교도직"),
    // MILITARY(9, "군인"),
    // HEALTH_MEDICAL(10, "보건·의료직"),
    // ARTS_DESIGN_MEDIA(11, "예술·디자인·방송직"),
    // SPORTS_RECREATION(12, "스포츠·레크리에이션직"),
    // SECURITY_GUARD(13, "경호·경비직"),
    // CARE_SERVICE(14, "돌봄 서비스직(간병·육아)"),
    // CLEANING_PERSONAL_SERVICE(15, "청소 및 기타 개인서비스직"),
    // BEAUTY_WEDDING_SERVICE(16, "미용·예식 서비스직"),
    // TRAVEL_LODGING_ENTERTAINMENT(17, "여행·숙박·오락 서비스직"),
    // FOOD_SERVICE(18, "음식 서비스직"),
    // SALES(19, "영업·판매직"),
    // DRIVING_TRANSPORT(20, "운전·운송직"),
    // CONSTRUCTION_MINING(21, "건설·채굴직"),
    // FOOD_PROCESSING_PRODUCTION(22, "식품 가공·생산직"),
    // PRINT_WOOD_CRAFT_ETC_INSTALL_MAINT_PROD(23, "인쇄·목재·공예 및 기타 설치·정비·생산직"),
    // MANUFACTURING_SIMPLE(24, "제조 단순직"),
    // MACHINE_INSTALL_MAINT_PROD(25, "기계 설치·정비·생산직"),
    // METAL_MATERIAL_INSTALL_MAINT_PROD(26, "금속·재료 설치·정비·생산직(판금·단조·주조·용접·도장 등)"),
    // ELECTRICAL_ELECTRONIC_INSTALL_MAINT_PROD(27, "전기·전자 설치·정비·생산직"),
    // ICT_INSTALL_MAINT(28, "정보통신 설치·정비직"),
    // CHEM_ENV_INSTALL_MAINT_PROD(29, "화학·환경 설치·정비·생산직"),
    // TEXTILE_APPAREL_PROD(30, "섬유·의복 생산직"),
    // AGRI_FISHERY(31, "농림어업직");

    // 새로운 간단한 라벨들 (2~6글자)
    HUM_SOC_RESEARCH(0, "인문사회"),
    NAT_BIO_RESEARCH(1, "자연생명"),
    ICT_RND_ENGINEERING(2, "정보통신"),
    CONSTR_MINING_RND_ENGINEERING(3, "건설채굴"),
    MANUFACTURING_RND_ENGINEERING(4, "제조공학"),
    WELFARE_RELIGION(5, "사회복지"),
    EDUCATION(6, "교육"),
    LAW(7, "법률"),
    POLICE_FIRE_CORRECTION(8, "경찰소방"),
    MILITARY(9, "군인"),
    HEALTH_MEDICAL(10, "보건의료"),
    ARTS_DESIGN_MEDIA(11, "예술디자인"),
    SPORTS_RECREATION(12, "스포츠"),
    SECURITY_GUARD(13, "경호경비"),
    CARE_SERVICE(14, "돌봄서비스"),
    CLEANING_PERSONAL_SERVICE(15, "청소서비스"),
    BEAUTY_WEDDING_SERVICE(16, "미용예식"),
    TRAVEL_LODGING_ENTERTAINMENT(17, "여행숙박"),
    FOOD_SERVICE(18, "음식서비스"),
    SALES(19, "영업판매"),
    DRIVING_TRANSPORT(20, "운전운송"),
    CONSTRUCTION_MINING(21, "건설채굴"),
    FOOD_PROCESSING_PRODUCTION(22, "식품가공"),
    PRINT_WOOD_CRAFT_ETC_INSTALL_MAINT_PROD(23, "인쇄목재"),
    MANUFACTURING_SIMPLE(24, "제조단순"),
    MACHINE_INSTALL_MAINT_PROD(25, "기계정비"),
    METAL_MATERIAL_INSTALL_MAINT_PROD(26, "금속재료"),
    ELECTRICAL_ELECTRONIC_INSTALL_MAINT_PROD(27, "전기전자"),
    ICT_INSTALL_MAINT(28, "정보통신"),
    CHEM_ENV_INSTALL_MAINT_PROD(29, "화학환경"),
    TEXTILE_APPAREL_PROD(30, "섬유의복"),
    AGRI_FISHERY(31, "농림어업");

    companion object {
        private val codeMap = values().associateBy(InterestCategory::code)

        fun fromCode(code: Int): InterestCategory =
            codeMap[code] ?: throw IllegalArgumentException("[InterestCategory] Invalid code: $code")
    }
}
