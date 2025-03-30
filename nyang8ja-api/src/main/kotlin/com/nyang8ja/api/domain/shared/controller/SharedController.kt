package com.nyang8ja.api.domain.shared.controller

import com.nyang8ja.api.common.annotation.RequestUser
import com.nyang8ja.api.common.dto.DefaultResponse
import com.nyang8ja.api.common.dto.RequestUserInfo
import com.nyang8ja.api.domain.shared.dto.ShareLoggingRequestDto
import com.nyang8ja.api.domain.shared.service.ShareLogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/shared")
@Tag(name = "00. Common API")
class SharedController(
    private val commonService: com.nyang8ja.api.domain.shared.service.ShareLogService
) {
    @Operation(summary = "질문 공유하기 로깅", description = "공유하기 시 로깅용 API")
    @PostMapping("/share")
    fun shareLog(
        @RequestUser requestUser: com.nyang8ja.api.common.dto.RequestUserInfo,
        @Valid @RequestBody request: ShareLoggingRequestDto
    ): com.nyang8ja.api.common.dto.DefaultResponse {
        commonService.saveShareLog(request.resultId)
        return com.nyang8ja.api.common.dto.DefaultResponse()
    }
}