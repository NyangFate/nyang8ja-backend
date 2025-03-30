package com.nyang8ja.api.external.ai.client

import com.nyang8ja.api.common.exception.ExternalServerErrorBizException
import com.nyang8ja.api.external.ai.dto.*
import com.nyang8ja.api.external.ai.properties.AiServerProperties
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.nio.charset.StandardCharsets

@Component
@Profile("!local")
class AiClientV1(
    private val aiServerProperties: com.nyang8ja.api.external.ai.properties.AiServerProperties
): com.nyang8ja.api.external.ai.client.AiClient {
    private val restClient = RestClient.builder()
        .baseUrl(aiServerProperties.domain + aiServerProperties.basePath)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .requestInterceptor { request, body, execution ->
            val requestInfo = "Request: ${request.method} ${request.uri} - Body: ${String(body)}. Headers: ${request.headers}"
            val response: ClientHttpResponse = execution.execute(request, body)
            if (!response.statusCode.is2xxSuccessful) throw ExternalServerErrorBizException(responseLog("Error", response, requestInfo))
            response
        }
        .build()

    override fun chatClassification(request: AiChatCommonRequestDto): com.nyang8ja.api.external.ai.dto.AiChatClassifyResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, com.nyang8ja.api.external.ai.dto.AiChatClassifyResponseDto>(
            aiServerProperties.classifyChatPath,
            request
        )
    }

    override fun chatCasually(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatCommonResponseDto>(
            aiServerProperties.generalChatPath,
            request
        )
    }

    override fun chatInappropriate(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatCommonResponseDto>(
            aiServerProperties.inappropriateChatPath,
            request
        )
    }

    override fun chatTarotQuestion(request: AiChatCommonRequestDto): AiChatCommonResponseDto {
        return requestPostToAiServer<AiChatCommonRequestDto, AiChatCommonResponseDto>(
            aiServerProperties.tarotQuestionChatPath,
            request
        )
    }

    override fun chatTarotResult(request: AiTarotResultRequestDto): AiTarotResultResponseDto {
        return requestPostToAiServer<AiTarotResultRequestDto, AiTarotResultResponseDto>(
            aiServerProperties.tarotResultPath,
            request
        )
    }

    override fun tarotQuestionSummary(request: com.nyang8ja.api.external.ai.dto.AiTarotQuestionSummaryRequestDto): AiTarotQuestionSummaryResponseDto {
        return requestPostToAiServer<com.nyang8ja.api.external.ai.dto.AiTarotQuestionSummaryRequestDto, AiTarotQuestionSummaryResponseDto>(
            aiServerProperties.tarotQuestionSummary,
            request
        )
    }

    override fun tarotFollowQuestion(request: AiTarotFollowQuestionRequestDto): AiTarotFollowQuestionResponseDto {
        return requestPostToAiServer<AiTarotFollowQuestionRequestDto, AiTarotFollowQuestionResponseDto>(
            aiServerProperties.tarotFollowQuestion,
            request
        )
    }

    private inline fun <reified Req : Any, reified Res : Any> requestPostToAiServer(path: String, request: Req): Res {
        val response = restClient.post()
            .uri(path)
            .body(request)
            .retrieve()
            .toEntity(Res::class.java)

        return response.body ?: throw ExternalServerErrorBizException("Failed to request to ai server")
    }

    private fun responseLog(errorType: String, response: ClientHttpResponse, requestInfo: String = ""): String {
        try {
            val responseBody = String(response.body.readAllBytes(), StandardCharsets.UTF_8)
            return """
                ${requestInfo.ifEmpty { "" }}
                AI 서버 응답 에러: [$errorType]
                Status Code: ${response.statusCode}
                Headers: ${response.headers}
                Body: $responseBody
            """.trimIndent()
        } catch (e: Exception) {
            return "Failed to log AI Server response details"
        }
    }
}