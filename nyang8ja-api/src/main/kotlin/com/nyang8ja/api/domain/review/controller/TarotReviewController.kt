package com.nyang8ja.api.domain.review.controller

import com.nyang8ja.api.common.annotation.RequestUser
import com.nyang8ja.api.common.dto.RequestUserInfo
import com.nyang8ja.api.domain.review.dto.CreateTarotReviewRequestDto
import com.nyang8ja.api.domain.review.dto.HasReviewedTarotResponseDto
import com.nyang8ja.api.domain.review.service.TarotReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/review")
@Tag(name = "03. Review API")
class TarotReviewController(
    private val tarotReviewService: TarotReviewService,
) {
    @Operation(summary = "타로 리뷰 작성여부 체크", description = "해당 타로결과의 리뷰 작성여부를 리턴합니다")
    @GetMapping("/exist/{tarotResultId}")
    fun hasReviewedTarot(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo,
        @PathVariable tarotResultId: Long,
    ): HasReviewedTarotResponseDto {
        return HasReviewedTarotResponseDto(tarotReviewService.hasReviewedTarot(requestUser.userKey, tarotResultId))
    }

    @Operation(summary = "타로 리뷰 작성", description = "리턴값은 없습니다.")
    @PostMapping("/create")
    fun createTarotReview(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo,
        @RequestBody createTarotReviewRequestDto: com.nyang8ja.api.domain.review.dto.CreateTarotReviewRequestDto,
    ) {
        tarotReviewService.createTarotReview(requestUser.userKey, createTarotReviewRequestDto.tarotResultId, createTarotReviewRequestDto.grade)
    }
}