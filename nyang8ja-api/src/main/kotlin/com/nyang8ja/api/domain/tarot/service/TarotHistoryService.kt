package com.nyang8ja.api.domain.tarot.service

import com.nyang8ja.api.domain.tarot.dto.TarotHistoryListResponseDto
import com.nyang8ja.api.domain.tarot.dto.TarotHistoryEventDto

interface TarotHistoryService {
    fun getTarotHistoryList(userKey: String): TarotHistoryListResponseDto

    fun saveTarotHistory(request: TarotHistoryEventDto)
}