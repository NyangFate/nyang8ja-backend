package com.nyang8ja.api.external.social.dto

import com.nyang8ja.api.common.enums.LoginType

data class SocialUserInfo(
    val socialType: com.nyang8ja.api.common.enums.LoginType,
    val socialId: String,
    val email: String? = null,
    val name: String? = null,
    val profileImage: String? = null,
)
