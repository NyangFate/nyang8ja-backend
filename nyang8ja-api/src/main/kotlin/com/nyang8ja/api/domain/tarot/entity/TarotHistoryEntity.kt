package com.nyang8ja.api.domain.tarot.entity

import com.nyang8ja.api.common.enums.TarotInfo
import com.nyang8ja.api.domain.chat.entity.TarotChatRoomEntity
import com.nyang8ja.api.domain.common.entity.BaseEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment


@Comment("타로 결과에 대한 히스토리")
@Entity
@Table(name = "tarot_history")
class TarotHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("히스토리의 주인")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val user: com.nyang8ja.api.domain.user.entity.UserEntity,

    @Comment("이 요약이 생겨난 채팅방")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: TarotChatRoomEntity,

    @Comment("요약한 질문 내용")
    @Column(name = "question_summary", nullable = false, columnDefinition = "VARCHAR(255)")
    val questionSummary: String,

    @Comment("타로 결과 ID")
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tarot_result_id", nullable = true)
    val tarotResult: com.nyang8ja.api.domain.tarot.entity.TarotResultEntity,
): com.nyang8ja.api.domain.common.entity.BaseEntity()

