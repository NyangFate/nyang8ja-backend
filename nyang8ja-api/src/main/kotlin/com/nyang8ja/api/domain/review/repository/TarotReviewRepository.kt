package com.nyang8ja.api.domain.review.repository

import com.nyang8ja.api.domain.review.entity.TarotReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TarotReviewRepository: JpaRepository<com.nyang8ja.api.domain.review.entity.TarotReviewEntity, Long> {
    fun existsByUserIdAndTarotResultId(userId: Long, tarotResultId: Long): Boolean
}