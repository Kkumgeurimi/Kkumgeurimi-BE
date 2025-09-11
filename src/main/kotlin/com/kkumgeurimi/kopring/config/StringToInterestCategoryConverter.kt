package com.kkumgeurimi.kopring.config

import com.kkumgeurimi.kopring.domain.common.InterestCategory
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToInterestCategoryConverter : Converter<String, InterestCategory> {
    override fun convert(source: String): InterestCategory? {
        return source.toIntOrNull()?.let { InterestCategory.fromCode(it) }
    }
}
