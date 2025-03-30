package com.nyang8ja.api.domain.chat.service

import com.nyang8ja.api.domain.chat.dto.ChatMessageListResponseDto
import com.nyang8ja.api.domain.chat.dto.ChatMessageResponseDto
import com.nyang8ja.api.domain.chat.dto.ChatMessageSendRequestDto
import com.nyang8ja.api.domain.chat.dto.ChatRoomCreateResponseDto
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity

interface ChatService {
    fun createChatRoom(tempUserKey: String): ChatRoomCreateResponseDto
    fun getChatRoomMessages(tempUserKey: String, roomId: Long): ChatMessageListResponseDto
    fun sendChatMessage(tempUserKey: String, request: ChatMessageSendRequestDto): ChatMessageResponseDto
}