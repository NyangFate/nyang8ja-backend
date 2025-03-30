package com.nyang8ja.api.common.dto

data class GlobalResponse(
    val requestId: String,
    val requestTime: String,
    val success : Boolean,
    val data: Any? = null,
)
