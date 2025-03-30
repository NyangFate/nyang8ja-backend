package com.nyang8ja.api.domain.tarot.entity

import com.nyang8ja.api.domain.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Comment("타로결과에 대한 추천 꼬리질문")
@Entity
@Table(name = "tarot_result_follow_questions")
class TarotResultFollowQuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarot_result_id", nullable = false)
    val tarotResult: TarotResultEntity,

    @Lob
    @Column(name = "questions_string", nullable = true, columnDefinition = "TEXT")
    val questionsString: String?,
): com.nyang8ja.api.domain.common.entity.BaseEntity() {
    companion object {
        fun create(tarotResult: TarotResultEntity, questions: List<String>): TarotResultFollowQuestionEntity {
            return TarotResultFollowQuestionEntity(
                tarotResult = tarotResult,
                questionsString = questions.joinToString("\n"))
        }
    }

    fun getQuestions(): List<String> {
        return questionsString?.split("\n") ?: emptyList()
    }
}