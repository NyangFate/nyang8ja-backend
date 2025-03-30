package com.nyang8ja.api.common.exception

class ExternalServerErrorBizException(log: String): BizException(ErrorCode.EXTERNAL_SERVER_ERROR, log)