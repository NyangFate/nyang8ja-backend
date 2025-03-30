package com.nyang8ja.api.common.exception

class InternalServerErrorBizException(log: String): BizException(ErrorCode.INTERNAL_SERVER_ERROR, log)