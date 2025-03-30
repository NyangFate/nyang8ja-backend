package com.nyang8ja.api.external.ai.dto

import com.nyang8ja.api.common.enums.TarotInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AiTarotResultRequestDto(
    val chatRoomId: String,
    val tarotCard: TarotInfo
)