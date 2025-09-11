package com.kkumgeurimi.kopring.domain.common

enum class ProgramType(val code: Int, val label: String) {
    FIELD_COMPANY(0, "현장직업체험형"),
    JOB_PRACTICE(1, "직업실무체험형"),
    FIELD_VISIT(2, "현장견학형"),
    ACADEMIC(3, "학과체험형"),
    CAMP(4, "캠프형"),
    LECTURE(5, "강연형"),
    DIALOGUE(6, "대화형");

    companion object {
        private val codeMap = values().associateBy(ProgramType::code)
        fun fromCode(code: Int): ProgramType? = codeMap[code]
    }
}