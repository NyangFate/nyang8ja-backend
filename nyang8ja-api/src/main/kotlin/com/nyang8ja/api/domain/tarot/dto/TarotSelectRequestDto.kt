package com.nyang8ja.api.domain.tarot.dto

import com.nyang8ja.api.common.enums.TarotInfo
import io.swagger.v3.oas.annotations.media.Schema

data class TarotSelectRequestDto(
    @field:Schema(description = "채팅방 ID", example = "12345")
    val roomId: Long,
    @field:Schema(description = "선택한 타로 카드이름", example = "S_01")
    val tarotName: TarotInfo,
)
