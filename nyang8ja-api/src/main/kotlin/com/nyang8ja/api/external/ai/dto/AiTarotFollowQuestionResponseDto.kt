package com.nyang8ja.api.external.ai.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AiTarotFollowQuestionResponseDto(
    val followUpQuestion: List<String>
)
