package com.nyang8ja.api.domain.user.service.helper

import com.nyang8ja.api.common.enums.LoginType
import com.nyang8ja.api.common.exception.BadRequestBizException
import com.nyang8ja.api.domain.common.annotation.HelperService
import com.nyang8ja.api.domain.user.entity.UserEntity
import com.nyang8ja.api.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@HelperService
@Transactional(readOnly = true)
class UserHelperService(
    private val userRepository: UserRepository
) {
    fun getUserOrThrow(id: Long): com.nyang8ja.api.domain.user.entity.UserEntity {
        return userRepository.findByIdOrNull(id)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [id: $id]")
    }

    fun getUserOrThrow(tempUserKey: String): com.nyang8ja.api.domain.user.entity.UserEntity {
        return userRepository.findByUserKey(tempUserKey)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [tempUserKey: $tempUserKey]")
    }

    fun getUserOrThrow(type: com.nyang8ja.api.common.enums.LoginType, key: String): com.nyang8ja.api.domain.user.entity.UserEntity {
        if (type == com.nyang8ja.api.common.enums.LoginType.GUEST) return getUserOrThrow(key)
        return userRepository.findByLoginTypeAndSocialId(type, key)
            ?: throw BadRequestBizException("해당 유저가 존재하지 않습니다. [type: $type socialId: $key]")
    }

    fun getUserOrNull(type: com.nyang8ja.api.common.enums.LoginType, key: String): com.nyang8ja.api.domain.user.entity.UserEntity? {
        if (type == com.nyang8ja.api.common.enums.LoginType.GUEST) return userRepository.findByUserKey(key)
        return userRepository.findByLoginTypeAndSocialId(type, key)
    }
}