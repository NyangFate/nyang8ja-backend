package com.nyang8ja.api.domain.tarot.repository

import com.nyang8ja.api.domain.tarot.entity.TarotResultFollowQuestionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotResultFollowQuestionRepository: JpaRepository<TarotResultFollowQuestionEntity, Long> {
    fun findByTarotResultId(tarotResultId: Long): List<TarotResultFollowQuestionEntity>
}