package com.nyang8ja.api.external.social.client

import com.nyang8ja.api.external.social.dto.SocialUserInfo

interface OidcStrategy {
    fun authenticate(token: String): SocialUserInfo
}