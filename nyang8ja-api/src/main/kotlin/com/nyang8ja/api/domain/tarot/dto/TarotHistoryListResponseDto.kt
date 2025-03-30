package com.nyang8ja.api.domain.tarot.dto

import com.nyang8ja.api.domain.tarot.entity.TarotHistoryEntity
import io.swagger.v3.oas.annotations.media.Schema

data class TarotHistoryListResponseDto(
    @field:Schema(description = "타로 히스토리 정보들")
    val results: List<com.nyang8ja.api.domain.tarot.dto.TarotHistoryResponseDto>
) {
    companion object {
        fun of(
            results: List<com.nyang8ja.api.domain.tarot.entity.TarotHistoryEntity>
        ): TarotHistoryListResponseDto {
            return TarotHistoryListResponseDto(
                results = results.map { com.nyang8ja.api.domain.tarot.dto.TarotHistoryResponseDto.of(it) }
            )
        }
    }
}
