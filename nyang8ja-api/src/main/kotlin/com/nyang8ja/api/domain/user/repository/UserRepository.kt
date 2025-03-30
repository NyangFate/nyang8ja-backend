package com.nyang8ja.api.domain.user.repository

import com.nyang8ja.api.common.enums.LoginType
import com.nyang8ja.api.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<com.nyang8ja.api.domain.user.entity.UserEntity, Long> {
    fun findByUserKey(userKey: String): com.nyang8ja.api.domain.user.entity.UserEntity?

    fun findByLoginTypeAndSocialId(loginType: com.nyang8ja.api.common.enums.LoginType, socialId: String): com.nyang8ja.api.domain.user.entity.UserEntity?
}