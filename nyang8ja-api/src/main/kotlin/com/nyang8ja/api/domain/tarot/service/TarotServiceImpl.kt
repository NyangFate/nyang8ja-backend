package com.nyang8ja.api.domain.tarot.service

import com.nyang8ja.api.common.enums.TarotInfo
import com.nyang8ja.api.domain.chat.dto.ChatMessageResponseDto
import com.nyang8ja.api.domain.chat.entity.TarotChatMessageEntity
import com.nyang8ja.api.domain.chat.repository.TarotChatMessageRepository
import com.nyang8ja.api.domain.chat.service.helper.ChatHelperService
import com.nyang8ja.api.domain.tarot.dto.*
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import com.nyang8ja.api.domain.tarot.entity.TarotResultFollowQuestionEntity
import com.nyang8ja.api.domain.tarot.repository.TarotQuestionRepository
import com.nyang8ja.api.domain.tarot.repository.TarotResultFollowQuestionRepository
import com.nyang8ja.api.domain.tarot.repository.TarotResultRepository
import com.nyang8ja.api.domain.tarot.service.helper.TarotHelperService
import com.nyang8ja.api.domain.user.service.helper.UserHelperService
import com.nyang8ja.api.external.ai.client.AiClient
import com.nyang8ja.api.external.ai.dto.AiTarotFollowQuestionRequestDto
import com.nyang8ja.api.external.ai.dto.AiTarotResultRequestDto
import com.nyang8ja.api.external.ai.dto.AiTarotResultResponseDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TarotServiceImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val aiClient: com.nyang8ja.api.external.ai.client.AiClient,
    private val userHelperService: UserHelperService,
    private val chatHelperService: ChatHelperService,
    private val tarotHelperService: TarotHelperService,
    private val tarotQuestionRepository: TarotQuestionRepository,
    private val tarotResultRepository: TarotResultRepository,
    private val tarotChatMessageRepository: TarotChatMessageRepository,
    private val tarotResultFollowQuestionRepository: TarotResultFollowQuestionRepository,
): com.nyang8ja.api.domain.tarot.service.TarotService {
    @Transactional
    override fun selectTarot(tempUserKey: String, request: TarotSelectRequestDto): ChatMessageResponseDto {
        // validation
        val user = userHelperService.getUserOrThrow(tempUserKey)
        val room = chatHelperService.getChatRoomOrThrow(request.roomId, user)

        // tarotResult 생성
        val tarotResultInfo = aiClient.chatTarotResult(
            AiTarotResultRequestDto(room.id.toString(), request.tarotName)
        )

        // tarotResult 저장
        val tarotResult = createTarotResultEntity(tarotResultInfo, request.tarotName)
        val replyChatMessage = TarotChatMessageEntity.createTarotResultChatMessage(room, request.tarotName, tarotResult)
        tarotResultRepository.save(tarotResult)
        tarotChatMessageRepository.save(replyChatMessage)

        // tarotHistory 이벤트
        eventPublisher.publishEvent(TarotHistoryEventDto(user.userKey, tarotResult.id))

        return ChatMessageResponseDto.of(replyChatMessage)
    }

    @Transactional
    override fun getTarotResult(userKey: String?, tarotResultId: Long): com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto {
        val tarotResult = tarotHelperService.getTarotResultOrThrow(tarotResultId)
        val tarotResultMessage = chatHelperService.getTarotResultMessageOrThrow(tarotResult)

        val chatRoom = tarotResultMessage.chatRoom
        val recentTarotQuestion = chatHelperService.getLatestUserTarotQuestionOrThrow(chatRoom, tarotResult)
        val isTarotResultOwner = chatRoom.user.isOwning(userKey)

        return com.nyang8ja.api.domain.tarot.dto.TarotResultResponseDto.of(tarotResult, recentTarotQuestion.message, isTarotResultOwner)
    }

    @Transactional
    override fun getRecommendTarotQuestions(): com.nyang8ja.api.domain.tarot.dto.RecommendTarotQuestionListResponseDto {
        val questionList = tarotQuestionRepository.findByOrderByReferenceCountDesc(Limit.of(20))
            .shuffled().take(10)
        return com.nyang8ja.api.domain.tarot.dto.RecommendTarotQuestionListResponseDto(
            questionList.map { RecommendTarotQuestionResponseDto.of(it) }
        )
    }

    @Transactional
    override fun getFollowTarotQuestions(request: com.nyang8ja.api.domain.tarot.dto.FollowTarotQuestionRequestDto): FollowTarotQuestionListResponseDto {
        val chatRoom = chatHelperService.getChatRoomOrThrow(request.chatRoomId)

        val tarotFollowQuestions = getFollowQuestions(request.chatRoomId, request.tarotResultId)

        return FollowTarotQuestionListResponseDto(
            tarotFollowQuestions
        )
    }

    private fun getFollowQuestions(chatRoomId: Long, tarotResultId: Long): List<String> {
        val chatRoom = chatHelperService.getChatRoomOrThrow(chatRoomId)
        val tarotResult = tarotHelperService.getTarotResultOrThrow(tarotResultId)

        return tarotHelperService.getTarotResultFollowQuestionsOrNull(tarotResultId)
                ?: aiClient.tarotFollowQuestion( AiTarotFollowQuestionRequestDto(chatRoom.id.toString()))
                    .followUpQuestion
                    .apply {
                        tarotResultFollowQuestionRepository.save(
                            TarotResultFollowQuestionEntity.create(tarotResult, this)
                        )
                    }
    }

    private fun createTarotResultEntity(
        tarotResultInfo: AiTarotResultResponseDto,
        tarotName: TarotInfo
    ): TarotResultEntity =
        TarotResultEntity(
            tarot = tarotName,
            type = tarotResultInfo.type,
            cardValueSummary = tarotResultInfo.summaryOfDescriptionOfCard,
            cardValueDescription = tarotResultInfo.descriptionOfCard,
            answerSummary = tarotResultInfo.summaryOfAnalysis,
            answerDescription = tarotResultInfo.analysis,
            adviceSummary = tarotResultInfo.summaryOfAdvice,
            adviceDescription = tarotResultInfo.advice,
            comprehensiveSummary = tarotResultInfo.comprehensiveSummary
        )
}