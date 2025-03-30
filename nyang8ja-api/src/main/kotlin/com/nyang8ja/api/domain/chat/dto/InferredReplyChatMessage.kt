package com.nyang8ja.api.domain.chat.dto

import com.nyang8ja.api.common.enums.MessageSender
import com.nyang8ja.api.common.enums.MessageType
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity

data class InferredReplyChatMessage(
    val message: String,
    val messageType: MessageType,
    val senderType: MessageSender = MessageSender.SYSTEM
) {
    fun toChatMessageEntity(chatRoom: TarotChatRoomEntity): TarotChatMessageEntity =
        TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = messageType,
            senderType = senderType,
            message = message,
        )
}
