package com.nyang8ja.api.domain.tarot.repository

import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotResultRepository: JpaRepository<TarotResultEntity, Long> {
}