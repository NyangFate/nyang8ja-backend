package com.nyang8ja.api.domain.user.entity

import com.nyang8ja.api.common.enums.LoginType
import com.nyang8ja.api.domain.common.entity.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Comment
import java.util.*


@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Comment("사용자 고유 식별자 (UUID)")
    @Column(name = "user_key", nullable = false, unique = true, updatable = false, columnDefinition = "VARCHAR(36)")
    var userKey: String = UUID.randomUUID().toString(),

    @Comment("유저 로그인 정보")
    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = true, columnDefinition = "VARCHAR(50)")
    var loginType: com.nyang8ja.api.common.enums.LoginType? = com.nyang8ja.api.common.enums.LoginType.GUEST,

    @Comment("소셜 로그인 시 해당 소셜 내 유저 식별자")
    @Column(name = "social_id", nullable = true, columnDefinition = "VARCHAR(255)")
    val socialId: String? = null,
): com.nyang8ja.api.domain.common.entity.BaseEntity() {
    fun isOwning(userKey: String?): Boolean {
        return this.userKey == userKey
    }
}