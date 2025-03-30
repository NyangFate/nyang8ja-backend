package com.nyang8ja.api.domain.chat.service.helper

import com.nyang8ja.api.common.exception.BadRequestBizException
import com.nyang8ja.api.common.exception.ForbiddenBizException
import com.nyang8ja.api.common.exception.InternalServerErrorBizException
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.chat.repository.TarotChatMessageRepository
import com.nyang8ja.api.domain.chat.repository.TarotChatRoomRepository
import com.nyang8ja.api.domain.common.annotation.HelperService
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class ChatHelperService(
    private val tarotChatRoomRepository: TarotChatRoomRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository
) {
    fun getChatRoomOrThrow(roomId: Long): TarotChatRoomEntity {
        return tarotChatRoomRepository.findById(roomId)
            .orElseThrow { BadRequestBizException("채팅방이 존재하지 않습니다. [id: $roomId]") }
    }

    fun getChatRoomOrThrow(roomId: Long, user: com.nyang8ja.api.domain.user.entity.UserEntity): TarotChatRoomEntity {
        return tarotChatRoomRepository.findByIdAndUser(roomId, user)
            ?: throw ForbiddenBizException("해당 유저에게 채팅방 접근권한이 존재하지 않습니다. [userId: ${user.id}, roomId: $roomId]")
    }

    fun getTarotResultMessageOrThrow(tarotResult: TarotResultEntity): TarotChatMessageEntity {
        return tarotChatMessageRepository.findByTarotResult(tarotResult)
            ?: throw InternalServerErrorBizException("타로 결과 ID를 담은 메세지를 찾을 수 없습니다. [tarotResultId: ${tarotResult.id}]")
    }

    fun getLatestUserTarotQuestionOrThrow(room: TarotChatRoomEntity, result: TarotResultEntity): TarotChatMessageEntity {
        return tarotChatMessageRepository.findLatestUserTarotQuestionBeforeResult(room, result)
            ?: throw BadRequestBizException("현재 채팅방에 타로 질문이 존재하지 않습니다. [roomId: ${room.id}]")
    }

    fun getLatestUserTarotQuestionOrThrow(roomId: Long): TarotChatMessageEntity {
        return tarotChatMessageRepository.findLatestUserTarotQuestion(roomId)
            ?: throw BadRequestBizException("현재 채팅방에 타로 질문이 존재하지 않습니다. [roomId: $roomId]")
    }
}