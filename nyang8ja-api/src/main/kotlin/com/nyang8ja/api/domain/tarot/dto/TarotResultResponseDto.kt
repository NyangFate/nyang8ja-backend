package com.nyang8ja.api.domain.tarot.dto

import com.nyang8ja.api.common.enums.TarotInfo
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import io.swagger.v3.oas.annotations.media.Schema

data class TarotResultResponseDto(
    @field:Schema(description = "사용자가 선택한 타로카드", example = "S_01")
    val tarot: TarotInfo,
    @field:Schema(description = "질문 카테고리", example = "연애")
    val type: String,
    @field:Schema(description = "타로결과 요약", example = "연애")
    val summary: String,
    @field:Schema(description = "카드의 의미")
    val cardValue: com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.CardValue,
    @field:Schema(description = "질문에 대한 대답")
    val answer: com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.Answer,
    @field:Schema(description = "조언")
    val advice: com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.Advice,
    @field:Schema(description = "요청자가 해당 타로 결과의 주인인지")
    val isOwner: Boolean = false
) {
    companion object {
        fun of(
            tarotResultEntity: TarotResultEntity,
            questionMessage: String,
            isOwner: Boolean = false
        ): com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto {
            return com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto(
                tarot = tarotResultEntity.tarot,
                type = tarotResultEntity.type,
                summary = tarotResultEntity.comprehensiveSummary,
                cardValue = com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.CardValue(
                    summary = tarotResultEntity.cardValueSummary,
                    description = tarotResultEntity.cardValueDescription
                ),
                answer = com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.Answer(
                    summary = tarotResultEntity.answerSummary,
                    description = tarotResultEntity.answerDescription,
                    question = questionMessage
                ),
                advice = com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.Advice(
                    summary = tarotResultEntity.adviceSummary,
                    description = tarotResultEntity.adviceDescription
                ),
                isOwner = isOwner
            )
        }
    }

    data class CardValue(
        val summary: String,
        val description: String
    )

    data class Answer(
        val summary: String,
        val description: String,
        val question: String
    )

    data class Advice(
        val summary: String,
        val description: String
    )
}