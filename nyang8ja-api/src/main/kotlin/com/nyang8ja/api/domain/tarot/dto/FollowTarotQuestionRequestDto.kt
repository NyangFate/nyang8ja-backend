package com.nyang8ja.api.domain.tarot.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FollowTarotQuestionRequestDto(
    @field:Schema(description = "현재 참여중인 채팅방 ID", example = "9")
    val chatRoomId: Long,
    @field:Schema(description = "타로 결과 ID", example = "1")
    val tarotResultId: Long
)