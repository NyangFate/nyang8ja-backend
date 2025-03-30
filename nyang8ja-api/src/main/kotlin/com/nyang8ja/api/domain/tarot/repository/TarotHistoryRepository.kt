package com.nyang8ja.api.domain.tarot.repository

import com.nyang8ja.api.domain.tarot.entity.TarotHistoryEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface TarotHistoryRepository: JpaRepository<com.nyang8ja.api.domain.tarot.entity.TarotHistoryEntity, Long> {
    @EntityGraph(attributePaths = ["chatRoom", "tarotResult"])
    fun findAllByUserOrderByCreatedAtDesc(user: com.nyang8ja.api.domain.user.entity.UserEntity): List<com.nyang8ja.api.domain.tarot.entity.TarotHistoryEntity>
}