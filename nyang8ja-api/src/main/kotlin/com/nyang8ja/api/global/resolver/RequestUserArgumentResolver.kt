package com.nyang8ja.api.global.resolver

import com.nyang8ja.api.common.annotation.RequestUser
import com.nyang8ja.api.common.dto.RequestUserInfo
import com.nyang8ja.api.common.enums.ServiceRole
import com.nyang8ja.api.common.util.JwtUtil
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class RequestUserArgumentResolver(
    private val jwtUtil: JwtUtil
): HandlerMethodArgumentResolver {
    private val guestUserHeader = "X-Guest-ID"
    private val authorizationHeader = "Authorization"

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(RequestUser::class.java) && parameter.parameterType == com.nyang8ja.api.common.dto.RequestUserInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        webRequest.getHeader(guestUserHeader)
            ?.let {
                return com.nyang8ja.api.common.dto.RequestUserInfo(it, role = ServiceRole.GUEST)
            }

        webRequest.getHeader(authorizationHeader)
            ?.let {
                val serviceToken = jwtUtil.validateServiceToken(it)
                return com.nyang8ja.api.common.dto.RequestUserInfo(
                    userKey = serviceToken.userKey,
                    role = serviceToken.role
                )
            }

        return null
    }
}