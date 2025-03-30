package com.nyang8ja.api.global.filter

import com.nyang8ja.api.common.dto.DefaultResponse
import com.nyang8ja.api.common.dto.GlobalResponse
import com.nyang8ja.api.common.exception.BizException
import com.nyang8ja.api.common.extension.alertMessage
import com.nyang8ja.api.common.extension.getRequestId
import com.nyang8ja.api.common.extension.getRequestTime
import com.nyang8ja.api.common.extension.getRequestUri
import com.nyang8ja.api.external.notification.client.BizNotificationClient
import com.nyang8ja.api.external.notification.dto.BizNotificationType
import com.nyang8ja.api.external.notification.dto.DefaultNotificationMessage
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionHandleFilter(
    private val objectMapper: ObjectMapper,
    private val bizNotificationClient: com.nyang8ja.api.external.notification.client.BizNotificationClient
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BizException) {
            val message = e.alertMessage(e.errorCode.code, e.log)
            val globalResponse = createGlobalResponse(e.message)

            logger.warn(message)
            bizNotificationClient.sendUsual(
                createNotificationMessage(message),
                BizNotificationType.INFO
            )

            response.status = e.errorCode.code
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        } catch (e: Exception) {
            val errorMessage = e.alertMessage(500)
            val globalResponse = createGlobalResponse(e.message)

            logger.error(errorMessage)
            bizNotificationClient.sendError(
                createNotificationMessage(errorMessage)
            )

            response.status = 500
            response.writer.write(objectMapper.writeValueAsString(globalResponse))
        }
    }

    private fun createGlobalResponse(message: String?): com.nyang8ja.api.common.dto.GlobalResponse {
        return com.nyang8ja.api.common.dto.GlobalResponse(
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            success = false,
            data = com.nyang8ja.api.common.dto.DefaultResponse(message ?: "메세지를 알 수 없는 에러가 발생했습니다.")
        )
    }

    private fun createNotificationMessage(message: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            requestUri = getRequestUri(),
        )
    }
}