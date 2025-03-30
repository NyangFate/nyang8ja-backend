package com.nyang8ja.api.domain.auth.controller

import com.nyang8ja.api.common.annotation.UserKeyIgnored
import com.nyang8ja.api.domain.auth.dto.AppleSignInRequestDto
import com.nyang8ja.api.domain.auth.dto.KakaoSignInRequestDto
import com.nyang8ja.api.domain.auth.dto.TokenResponseDto
import com.nyang8ja.api.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/auth")
@Tag(name = "00. Auth API")
class AuthController(
    private val authService: com.nyang8ja.api.domain.auth.service.AuthService
) {
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 합니다")
    @UserKeyIgnored
    @PostMapping("/kakao")
    fun kakaoSignIn(
        @Valid @RequestBody request: KakaoSignInRequestDto
    ): com.nyang8ja.api.domain.auth.dto.TokenResponseDto {
        return authService.kakaoSignIn(request.authorizationCode)
    }

    @Operation(summary = "애플 로그인", description = "애플 로그인을 합니다")
    @UserKeyIgnored
    @PostMapping("/apple")
    fun appleSignIn(
        @Valid @RequestBody request: com.nyang8ja.api.domain.auth.dto.AppleSignInRequestDto
    ): com.nyang8ja.api.domain.auth.dto.TokenResponseDto {
        return authService.appleSignIn(request.idToken)
    }
}