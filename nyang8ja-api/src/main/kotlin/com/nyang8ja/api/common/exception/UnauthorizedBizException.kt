package com.nyang8ja.api.common.exception

class UnauthorizedBizException(log: String): BizException(ErrorCode.UNAUTHORIZED, log)
