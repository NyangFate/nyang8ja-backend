package com.nyang8ja.api.domain.shared.service

import com.nyang8ja.api.domain.shared.entity.ShareLogEntity
import com.nyang8ja.api.domain.shared.repository.ShareLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SharedServiceImpl(
    private val shareLogRepository: ShareLogRepository
): com.nyang8ja.api.domain.shared.service.ShareLogService {
    @Transactional
    override fun saveShareLog(tarotResultId: Long) {
        shareLogRepository.save(com.nyang8ja.api.domain.shared.entity.ShareLogEntity(tarotResultId = tarotResultId))
    }
}