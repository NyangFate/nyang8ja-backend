package com.nyang8ja.api.domain.tarot.repository

import com.nyang8ja.api.domain.tarot.entity.TarotQuestionEntity
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository

interface TarotQuestionRepository: JpaRepository<TarotQuestionEntity, Long> {
    fun findByOrderByReferenceCountDesc(limit: Limit): List<TarotQuestionEntity>

    fun findByQuestion(question: String): List<TarotQuestionEntity>
}