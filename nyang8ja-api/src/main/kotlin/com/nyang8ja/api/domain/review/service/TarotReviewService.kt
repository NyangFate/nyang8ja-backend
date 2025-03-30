package com.nyang8ja.api.domain.review.service

import com.nyang8ja.api.domain.review.enums.TarotReviewGrade

interface TarotReviewService {
    fun createTarotReview(userKey: String, tarotResultId:Long, grade: TarotReviewGrade)
    fun hasReviewedTarot(userKey: String, tarotResultId:Long): Boolean
}