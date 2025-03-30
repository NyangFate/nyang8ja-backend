package com.nyang8ja.api.domain.chat.dto

import com.nyang8ja.api.common.enums.MessageType

data class RequestResponseChatMessage(
    val requestMessageType: MessageType,
    val requestMessage: String,
    val responseMessageType: MessageType,
    val responseMessage: String,
)
