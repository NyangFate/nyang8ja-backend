package com.nyang8ja.api.domain.chat.repository

import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TarotChatRoomRepository: JpaRepository<TarotChatRoomEntity, Long> {
    fun findByUser(user: com.nyang8ja.api.domain.user.entity.UserEntity): List<TarotChatRoomEntity>
    fun findByIdAndUser(id: Long, user: com.nyang8ja.api.domain.user.entity.UserEntity): TarotChatRoomEntity?
}