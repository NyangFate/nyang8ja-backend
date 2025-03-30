package com.nyang8ja.api.domain.user.service

import com.nyang8ja.api.domain.user.entity.UserEntity
import com.nyang8ja.api.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
): UserService {
    @Transactional
    override fun getOrCreateUser(tempUserKey: String): com.nyang8ja.api.domain.user.entity.UserEntity {
        val user = userRepository.findByUserKey(tempUserKey)
        return user ?: userRepository.save(com.nyang8ja.api.domain.user.entity.UserEntity(userKey = tempUserKey))
    }
}