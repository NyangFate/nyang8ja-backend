package com.nyang8ja.api.domain.chat.entity

import com.nyang8ja.api.domain.common.entity.BaseEntity
import com.nyang8ja.api.domain.user.entity.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "tarot_chat_rooms")
class TarotChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    val user: com.nyang8ja.api.domain.user.entity.UserEntity,
): com.nyang8ja.api.domain.common.entity.BaseEntity()