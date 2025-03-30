package com.nyang8ja.api.global.handler

import com.nyang8ja.api.common.dto.DefaultResponse
import com.nyang8ja.api.common.exception.BizException
import com.nyang8ja.api.common.extension.alertMessage
import com.nyang8ja.api.common.extension.getRequestId
import com.nyang8ja.api.common.extension.getRequestTime
import com.nyang8ja.api.common.extension.getRequestUri
import com.nyang8ja.api.external.notification.client.BizNotificationClient
import com.nyang8ja.api.external.notification.dto.BizNotificationType
import com.nyang8ja.api.external.notification.dto.DefaultNotificationMessage
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.lang.Nullable
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice(basePackages = ["com.nyang8ja.api"])
class GlobalExceptionHandler(
    val bizNotificationClient: com.nyang8ja.api.external.notification.client.BizNotificationClient
): ResponseEntityExceptionHandler() {
    /**
     * Validation 예외
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.bindingResult
            .fieldErrors.joinToString("\n") {
                "${it.field} 필드 : ${it.defaultMessage}"
            }
        return ResponseEntity
            .status(400)
            .body(com.nyang8ja.api.common.dto.DefaultResponse(errorMessage))
    }

    /**
     * JSON 파싱 예외(ex. enum 값이 잘못 들어온 경우)
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.message ?: "잘못된 요청입니다."
        return ResponseEntity
            .status(400)
            .body(com.nyang8ja.api.common.dto.DefaultResponse(errorMessage))
    }

    /**
     * 기타 Spring이 내부적으로 지원하는 예외
     * - 데이터 액세스, 빈 등과 관련된 예외
     */
    override fun handleExceptionInternal(
        ex: Exception,
        @Nullable body: Any?,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorMessage = ex.message ?: "잘못된 요청입니다."
        bizNotificationClient.sendError(createNotificationMessage(errorMessage))

        return ResponseEntity
            .status(500)
            .body(com.nyang8ja.api.common.dto.DefaultResponse(errorMessage))
    }

    /**
     * Biz 예외
     */
    @ExceptionHandler(BizException::class)
    fun handleBizException(e: BizException, request: HttpServletRequest): ResponseEntity<com.nyang8ja.api.common.dto.DefaultResponse> {
        val errorMessage = e.alertMessage(e.errorCode.code, e.log)
        logger.warn(errorMessage)
        bizNotificationClient.sendUsual(createNotificationMessage(errorMessage), BizNotificationType.INFO)

        return ResponseEntity
            .status(e.errorCode.code)
            .body(com.nyang8ja.api.common.dto.DefaultResponse(e.errorCode.message))
    }

    /**
     * 그 외 잡지 못한 예외
     */
    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(e: Exception, request: HttpServletRequest): ResponseEntity<com.nyang8ja.api.common.dto.DefaultResponse> {
        val errorMessage = e.alertMessage(500)
        logger.error(errorMessage)
        bizNotificationClient.sendError(createNotificationMessage(errorMessage))

        return ResponseEntity
            .status(500)
            .body(com.nyang8ja.api.common.dto.DefaultResponse(errorMessage))
    }

    private fun createNotificationMessage(message: String): DefaultNotificationMessage {
        return DefaultNotificationMessage(
            message = message,
            requestId = getRequestId(),
            requestTime = getRequestTime(),
            requestUri = getRequestUri()
        )
    }
}