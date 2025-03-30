package com.nyang8ja.api.domain.auth.service

import com.nyang8ja.api.domain.auth.dto.TokenResponseDto

interface AuthService {
    fun kakaoSignIn(authorizationCode: String): com.nyang8ja.api.domain.auth.dto.TokenResponseDto
    fun appleSignIn(idToken: String): com.nyang8ja.api.domain.auth.dto.TokenResponseDto
}