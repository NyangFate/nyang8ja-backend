package com.nyang8ja.api.external.notification.dto

data class DefaultNotificationMessage(
    val requestId: String,
    val requestTime: String,
    val requestUri: String,
    val message: String,
)
