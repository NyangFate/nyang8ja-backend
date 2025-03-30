package com.nyang8ja.api.domain.auth.service

import com.nyang8ja.api.common.dto.JwtUserInfo
import com.nyang8ja.api.common.util.JwtUtil
import com.nyang8ja.api.domain.auth.dto.TokenResponseDto
import com.nyang8ja.api.domain.user.entity.UserEntity
import com.nyang8ja.api.domain.user.repository.UserRepository
import com.nyang8ja.api.domain.user.service.helper.UserHelperService
import com.nyang8ja.api.external.social.client.OidcStrategy
import com.nyang8ja.api.external.social.dto.SocialUserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val kakaoClient: OidcStrategy,
    private val appleClient: OidcStrategy,
    private val jwtUtil: JwtUtil,
    private val userHelperService: UserHelperService,
    private val userRepository: UserRepository,
): com.nyang8ja.api.domain.auth.service.AuthService {
    @Transactional
    override fun kakaoSignIn(authorizationCode: String): com.nyang8ja.api.domain.auth.dto.TokenResponseDto {
        val socialUserInfo = kakaoClient.authenticate(authorizationCode)
        val user = getOrCreateUser(socialUserInfo)
        val accessToken = jwtUtil.generateServiceToken(JwtUserInfo(userKey = user.userKey))

        return com.nyang8ja.api.domain.auth.dto.TokenResponseDto(accessToken)
    }

    @Transactional
    override fun appleSignIn(idToken: String): com.nyang8ja.api.domain.auth.dto.TokenResponseDto {
        val socialUserInfo = appleClient.authenticate(idToken)
        val user = getOrCreateUser(socialUserInfo)
        val accessToken = jwtUtil.generateServiceToken(JwtUserInfo(userKey = user.userKey))

        return com.nyang8ja.api.domain.auth.dto.TokenResponseDto(accessToken)
    }

    private fun getOrCreateUser(socialUserInfo: SocialUserInfo): com.nyang8ja.api.domain.user.entity.UserEntity {
        return userHelperService.getUserOrNull(socialUserInfo.socialType, socialUserInfo.socialId)
            ?: userRepository.save(
                com.nyang8ja.api.domain.user.entity.UserEntity(
                    socialId = socialUserInfo.socialId,
                    loginType = socialUserInfo.socialType,
                )
            )
    }
}