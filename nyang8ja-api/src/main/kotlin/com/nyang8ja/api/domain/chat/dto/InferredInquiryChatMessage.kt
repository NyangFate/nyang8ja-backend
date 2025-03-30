package com.nyang8ja.api.domain.chat.dto

import com.nyang8ja.api.common.enums.MessageSender
import com.nyang8ja.api.common.enums.MessageType
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.user.entity.UserEntity

data class InferredInquiryChatMessage(
    val message: String,
    val messageType: MessageType,
    val referenceQuestionId: Long? = null,
    val senderType: MessageSender = MessageSender.USER,
) {
    fun toChatMessageEntity(chatRoom: TarotChatRoomEntity, user: com.nyang8ja.api.domain.user.entity.UserEntity): TarotChatMessageEntity =
        TarotChatMessageEntity(
            chatRoom = chatRoom,
            messageType = messageType,
            senderType = senderType,
            sender = user,
            message = message,
            referenceTarotQuestionId = referenceQuestionId,
        )
}
