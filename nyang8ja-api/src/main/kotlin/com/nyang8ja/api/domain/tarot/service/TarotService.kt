package com.nyang8ja.api.domain.tarot.service

import com.nyang8ja.api.domain.chat.dto.ChatMessageResponseDto
import com.nyang8ja.api.domain.tarot.dto.*

interface TarotService {
    fun selectTarot(tempUserKey: String, request: TarotSelectRequestDto): ChatMessageResponseDto
    fun getTarotResult(userKey: String?, tarotResultId: Long): com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto
    fun getRecommendTarotQuestions(): com.nyang8ja.api.domain.tarot.dto.RecommendTarotQuestionListResponseDto
    fun getFollowTarotQuestions(request: com.nyang8ja.api.domain.tarot.dto.FollowTarotQuestionRequestDto): FollowTarotQuestionListResponseDto
}