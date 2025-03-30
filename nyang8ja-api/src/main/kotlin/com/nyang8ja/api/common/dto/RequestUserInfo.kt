package com.nyang8ja.api.common.dto

import com.nyang8ja.api.common.enums.ServiceRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(hidden = true)
data class RequestUserInfo(
    @Schema(hidden = true)
    val userKey: String,
    @Schema(hidden = true)
    val role: ServiceRole = ServiceRole.GUEST,
)
