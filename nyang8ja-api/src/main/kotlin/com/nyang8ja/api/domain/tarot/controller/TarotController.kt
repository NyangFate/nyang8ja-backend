package com.nyang8ja.api.domain.tarot.controller

import com.nyang8ja.api.common.annotation.RequestUser
import com.nyang8ja.api.common.annotation.UserKeyIgnored
import com.nyang8ja.api.common.dto.RequestUserInfo
import com.nyang8ja.api.domain.chat.dto.ChatMessageResponseDto
import com.nyang8ja.api.domain.tarot.dto.*
import com.nyang8ja.api.domain.tarot.service.TarotHistoryService
import com.nyang8ja.api.domain.tarot.service.TarotService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/tarot")
@Tag(name = "02. Tarot API")
class TarotController(
    private val tarotService: com.nyang8ja.api.domain.tarot.service.TarotService,
    private val tarotHistoryService: TarotHistoryService
) {
    @Operation(summary = "타로 카드 선택", description = "타로 결과 id를 담은 채팅메세지로 응답합니다.")
    @PostMapping("/select")
    fun selectTarot(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo,
        @Valid @RequestBody request: TarotSelectRequestDto
    ): ChatMessageResponseDto {
        return tarotService.selectTarot(requestUser.userKey, request)
    }

    @Operation(summary = "타로 결과 가져오기", description = "타로 결과 id를 통해 타로 결과를 가져옵니다.")
    @UserKeyIgnored
    @GetMapping("/result/{resultId}")
    fun getTarotResult(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo?,
        @PathVariable resultId: Long
    ): com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto {
        return tarotService.getTarotResult(requestUser?.userKey, resultId)
    }

    @Operation(summary = "추천 타로 질문들 가져오기(4개)", description = "AI가 추천하는 타로 질문들을 가져옵니다.")
    @UserKeyIgnored
    @GetMapping("/question/recommends")
    fun getRecommendTarotQuestions(): com.nyang8ja.api.domain.tarot.dto.RecommendTarotQuestionListResponseDto {
        return tarotService.getRecommendTarotQuestions()
    }

    @Operation(summary = "추천 꼬리 질문들 가져오기(4개)", description = "AI가 추천하는 꼬리 질문들을 가져옵니다.")
    @UserKeyIgnored
    @GetMapping("/question/follow")
    fun getFollowTarotQuestions(
        request: com.nyang8ja.api.domain.tarot.dto.FollowTarotQuestionRequestDto
    ): FollowTarotQuestionListResponseDto {
        return tarotService.getFollowTarotQuestions(request)
    }

    @Operation(summary = "타로 히스토리 리스트", description = "본인이 받은 타로 히스토리 리스트를 가져옵니다.")
    @GetMapping("/history")
    fun getTarotHistory(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo,
    ): TarotHistoryListResponseDto {
        return tarotHistoryService.getTarotHistoryList(requestUser.userKey)
    }
}