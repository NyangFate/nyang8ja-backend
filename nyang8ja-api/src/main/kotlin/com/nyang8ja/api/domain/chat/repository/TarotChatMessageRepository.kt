package com.nyang8ja.api.domain.chat.repository

import com.nyang8ja.api.common.enums.MessageType
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TarotChatMessageRepository: JpaRepository<TarotChatMessageEntity, Long> {
    fun findAllByChatRoom(chatRoom: TarotChatRoomEntity): List<TarotChatMessageEntity>
    @Query(
        """
        SELECT m 
        FROM TarotChatMessageEntity m 
        WHERE m.chatRoom.id = :roomId AND  m.messageType = :messageType 
        ORDER BY m.createdAt DESC
        LIMIT 1
        """
    )
    fun findLatestUserTarotQuestion(
        roomId: Long,
        messageType: MessageType = MessageType.USER_TAROT_QUESTION
    ): TarotChatMessageEntity?

    @Query(
        """
        SELECT m
        FROM TarotChatMessageEntity m
        WHERE m.chatRoom = :room
          AND m.messageType = :messageType
          AND m.createdAt < (
              SELECT r.createdAt
              FROM TarotChatMessageEntity r
              WHERE r.tarotResult = :result
          )
        ORDER BY m.createdAt DESC
        LIMIT 1
        """
    )
    fun findLatestUserTarotQuestionBeforeResult(
        room: TarotChatRoomEntity,
        result: TarotResultEntity,
        messageType: MessageType = MessageType.USER_TAROT_QUESTION
    ): TarotChatMessageEntity?

    fun findByTarotResult(tarotResult: TarotResultEntity): TarotChatMessageEntity?
}