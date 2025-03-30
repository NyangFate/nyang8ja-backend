package com.nyang8ja.api.domain.tarot.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RecommendTarotQuestionListResponseDto(
    @field:Schema(description = "추천 질문 리스트")
    val questions: List<com.nyang8ja.api.domain.tarot.dto.RecommendTarotQuestionResponseDto>
)
