package com.nyang8ja.api.domain.chat.service

import com.nyang8ja.api.common.enums.MessageIntent
import com.nyang8ja.api.common.enums.MessageType
import com.nyang8ja.api.common.exception.BadRequestBizException
import com.nyang8ja.api.common.exception.InternalServerErrorBizException
import com.nyang8ja.api.common.extension.getRequestMetaData
import com.nyang8ja.api.domain.chat.dto.*
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.chat.repository.TarotChatMessageRepository
import com.nyang8ja.api.domain.chat.repository.TarotChatRoomRepository
import com.nyang8ja.api.domain.chat.service.helper.ChatHelperService
import com.nyang8ja.api.domain.tarot.entity.TarotQuestionEntity
import com.nyang8ja.api.domain.tarot.repository.TarotQuestionRepository
import com.nyang8ja.api.domain.user.service.UserService
import com.nyang8ja.api.domain.user.service.helper.UserHelperService
import com.nyang8ja.api.external.ai.client.AiClient
import com.nyang8ja.api.external.ai.dto.AiChatCommonRequestDto
import com.nyang8ja.api.external.notification.client.BizNotificationClient
import com.nyang8ja.api.external.notification.dto.DefaultNotificationMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatServiceImpl(
    private val aiClient: com.nyang8ja.api.external.ai.client.AiClient,
    private val bizNotificationClient: com.nyang8ja.api.external.notification.client.BizNotificationClient,
    private val userService: UserService,
    private val userHelperService: UserHelperService,
    private val tarotChatHelperService: ChatHelperService,
    private val tarotChatRoomRepository: TarotChatRoomRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository,
    private val tarotQuestionRepository: TarotQuestionRepository
): ChatService {
    @Transactional
    override fun createChatRoom(tempUserKey: String): ChatRoomCreateResponseDto {
        val user = userService.getOrCreateUser(tempUserKey)
        val newChatRoom = TarotChatRoomEntity(user = user)
        val welcomeChatMessage = TarotChatMessageEntity.createWelcomeChatMessage(newChatRoom)

        tarotChatRoomRepository.save(newChatRoom)
        tarotChatMessageRepository.save(welcomeChatMessage)

        return ChatRoomCreateResponseDto(
            roomId = newChatRoom.id,
            message = ChatMessageResponseDto.of(welcomeChatMessage)
        )
    }

    @Transactional
    override fun getChatRoomMessages(tempUserKey: String, roomId: Long): ChatMessageListResponseDto {
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val chatRoom = tarotChatHelperService.getChatRoomOrThrow(roomId, user)
        val chatMessageList = tarotChatMessageRepository.findAllByChatRoom(chatRoom)

        return ChatMessageListResponseDto(
            chatMessageList.map { ChatMessageResponseDto.of(it) }
        )
    }

    @Transactional
    override fun sendChatMessage(tempUserKey: String, request: ChatMessageSendRequestDto): ChatMessageResponseDto {
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val chatRoom = tarotChatHelperService.getChatRoomOrThrow(request.roomId, user)
        validateIfRecommendQuestionExists(request)

        val inquiryChatMessage = inferInquiryChatMessage(chatRoom, request)
        val replyChatMessage = inferReplyChatMessage(chatRoom, inquiryChatMessage)

        addTarotQuestion(inquiryChatMessage)
        val inquiryChatMessageEntity = inquiryChatMessage.toChatMessageEntity(chatRoom, user)
        val replyChatMessageEntity = replyChatMessage.toChatMessageEntity(chatRoom)
        tarotChatMessageRepository.save(inquiryChatMessageEntity)
        tarotChatMessageRepository.save(replyChatMessageEntity)

        return ChatMessageResponseDto.of(replyChatMessageEntity)
    }

    private fun validateIfRecommendQuestionExists(request: ChatMessageSendRequestDto) {
        if (request.intent != MessageIntent.RECOMMEND_QUESTION) return
        request.referenceQuestionId ?: throw BadRequestBizException("MessageIntent.RECOMMEND_QUESTION 이지만 추천질문 ID가 존재하지 않습니다.")

        tarotQuestionRepository.findById(request.referenceQuestionId)
            .orElseThrow { BadRequestBizException("존재하지 않는 추천질문 ID(${request.referenceQuestionId}) 입니다.") }
            .apply { referenceCount += 1 }
    }

    private fun addTarotQuestion(inquiry: InferredInquiryChatMessage) {
        if (inquiry.messageType != MessageType.USER_TAROT_QUESTION || inquiry.referenceQuestionId != null) return
        tarotQuestionRepository.findByQuestion(inquiry.message)
            .firstOrNull()
            ?.let { it.referenceCount += 1 }
            ?: tarotQuestionRepository.save(TarotQuestionEntity(question = inquiry.message))
    }

    private fun inferInquiryChatMessage(
        chatRoom: TarotChatRoomEntity,
        request: ChatMessageSendRequestDto
    ): InferredInquiryChatMessage = when(request.intent) {
        MessageIntent.NORMAL -> {
            val chatClassification = aiClient.chatClassification(
                AiChatCommonRequestDto(chatRoom.id.toString(), request.message)
            )
            InferredInquiryChatMessage(request.message, MessageType.userMessageFrom(chatClassification.type))
        }
        MessageIntent.RECOMMEND_QUESTION ->
            InferredInquiryChatMessage(request.message, MessageType.USER_TAROT_QUESTION, request.referenceQuestionId)
        MessageIntent.TAROT_DECLINE ->
            InferredInquiryChatMessage(request.message, MessageType.USER_TAROT_QUESTION_DECLINE)
        MessageIntent.TAROT_ACCEPT ->
            InferredInquiryChatMessage(request.message, MessageType.USER_TAROT_QUESTION_ACCEPTANCE)
    }

    private fun inferReplyChatMessage(
        chatRoom: TarotChatRoomEntity,
        inquiry: InferredInquiryChatMessage
    ): com.nyang8ja.api.domain.chat.dto.InferredReplyChatMessage {
        val request = AiChatCommonRequestDto(chatRoom.id.toString(), inquiry.message)
        val replyMessage = when(inquiry.messageType) {
            MessageType.USER_INVALID_QUESTION -> aiClient.chatInappropriate(request).answer
                .also { sendInvalidChatAlert("${inquiry.message}\n\n$it") }
            MessageType.USER_FOLLOW_QUESTION,
            MessageType.USER_TAROT_QUESTION -> aiClient.chatTarotQuestion(request).answer
            MessageType.USER_NORMAL,
            MessageType.USER_TAROT_QUESTION_DECLINE -> aiClient.chatCasually(request).answer
            MessageType.USER_TAROT_QUESTION_ACCEPTANCE -> "너의 고민에 집중하면서\n카드를 한 장 뽑아봐."
            else -> throw InternalServerErrorBizException("사용자 대화유형을 잘못 추론하였습니다.${inquiry.messageType}")
        }
        return com.nyang8ja.api.domain.chat.dto.InferredReplyChatMessage(replyMessage, inquiry.messageType.replyType())
    }

    private fun sendInvalidChatAlert(message: String) {
        val requestMetadata = getRequestMetaData()
        bizNotificationClient.sendInvalidQuestion(
            DefaultNotificationMessage(
                message = message,
                requestId = requestMetadata.requestId,
                requestTime = requestMetadata.requestTime,
                requestUri = requestMetadata.requestUri
            )
        )
    }
}