package com.nyang8ja.api.common.dto

import com.nyang8ja.api.common.enums.ServiceRole

data class JwtUserInfo(
    val userKey: String,
    val role: ServiceRole = ServiceRole.USER,
)