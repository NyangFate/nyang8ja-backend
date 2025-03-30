package com.nyang8ja.api.domain.tarot.service.helper

import com.nyang8ja.api.common.exception.BadRequestBizException
import com.nyang8ja.api.domain.common.annotation.HelperService
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import com.nyang8ja.api.domain.tarot.repository.TarotHistoryRepository
import com.nyang8ja.api.domain.tarot.repository.TarotQuestionRepository
import com.nyang8ja.api.domain.tarot.repository.TarotResultFollowQuestionRepository
import com.nyang8ja.api.domain.tarot.repository.TarotResultRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class TarotHelperService(
    private val tarotResultRepository: TarotResultRepository,
    private val tarotResultFollowQuestionRepository: TarotResultFollowQuestionRepository,
) {
    fun getTarotResultOrThrow(tarotResultId: Long): TarotResultEntity {
        return tarotResultRepository.findByIdOrNull(tarotResultId)
            ?: throw BadRequestBizException("타로 결과를 찾을 수 없습니다. id: $tarotResultId")
    }

    fun getTarotResultFollowQuestionsOrNull(tarotResultId: Long): List<String>? {
        return tarotResultFollowQuestionRepository.findByTarotResultId(tarotResultId)
            .firstOrNull()
            ?.getQuestions()
    }
}