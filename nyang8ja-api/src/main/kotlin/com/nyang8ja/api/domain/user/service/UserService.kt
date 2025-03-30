package com.nyang8ja.api.domain.user.service

import com.nyang8ja.api.domain.user.entity.UserEntity

interface UserService {
    fun getOrCreateUser(tempUserKey: String): com.nyang8ja.api.domain.user.entity.UserEntity
}