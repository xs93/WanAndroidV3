package com.github.xs93.framework.network.exception


/**
 * 登录异常
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/1/18
 */
class LoginException(errorCode: Int, errorMessage: String?) : ApiException(errorCode, errorMessage)