package com.nyang8ja.api.external.notification.client

import com.nyang8ja.api.external.notification.dto.BizNotificationType
import com.nyang8ja.api.external.notification.dto.DefaultNotificationMessage
import org.springframework.stereotype.Component


interface BizNotificationClient {
    fun sendUsual(message: DefaultNotificationMessage, type: BizNotificationType)

    fun sendError(message: DefaultNotificationMessage)

    fun sendInvalidQuestion(message: DefaultNotificationMessage)
}