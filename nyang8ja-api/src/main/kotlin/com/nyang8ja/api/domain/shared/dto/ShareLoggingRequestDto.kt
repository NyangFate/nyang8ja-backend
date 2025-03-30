package com.nyang8ja.api.domain.shared.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ShareLoggingRequestDto(
    @field:Schema(description = "공유한 타로결과의 ID", example = "12345")
    val resultId: Long,
)
