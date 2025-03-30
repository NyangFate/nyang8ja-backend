package com.nyang8ja.api.common.exception

class BadRequestBizException(log: String): BizException(ErrorCode.BAD_REQUEST, log)
