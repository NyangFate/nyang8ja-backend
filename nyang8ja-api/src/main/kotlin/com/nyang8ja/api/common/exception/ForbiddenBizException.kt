package com.nyang8ja.api.common.exception

class ForbiddenBizException(log: String): BizException(ErrorCode.FORBIDDEN, log)