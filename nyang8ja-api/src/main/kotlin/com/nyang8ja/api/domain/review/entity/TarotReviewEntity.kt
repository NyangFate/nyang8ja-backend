package com.nyang8ja.api.domain.review.entity

import com.nyang8ja.api.domain.common.entity.BaseEntity
import com.nyang8ja.api.domain.review.enums.TarotReviewGrade
import com.nyang8ja.api.domain.tarot.entity.TarotResultEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Comment
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Comment("타로 이모지 리뷰를 위한 테이블")
@Entity
@Table(name = "tarot_reviews")
class TarotReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("유저가 선택한 리뷰 이모지")
    @Enumerated(EnumType.STRING)
    @Column(name = "tarot", nullable = false, columnDefinition = "VARCHAR(10)")
    val tarotGrade: TarotReviewGrade,

    @Comment("타로 결과 ID")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarot_result_id", nullable = false)
    val tarotResult: TarotResultEntity,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    val user: com.nyang8ja.api.domain.user.entity.UserEntity,
) : com.nyang8ja.api.domain.common.entity.BaseEntity()
