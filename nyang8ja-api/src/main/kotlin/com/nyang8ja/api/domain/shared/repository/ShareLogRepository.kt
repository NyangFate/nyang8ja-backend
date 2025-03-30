package com.nyang8ja.api.domain.shared.repository

import com.nyang8ja.api.domain.shared.entity.ShareLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ShareLogRepository: JpaRepository<com.nyang8ja.api.domain.shared.entity.ShareLogEntity, Long> {
}