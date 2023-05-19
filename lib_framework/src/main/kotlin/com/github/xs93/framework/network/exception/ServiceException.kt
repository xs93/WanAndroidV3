package com.github.xs93.framework.network.exception

/**
 *
 * 服务器返回的错误(请求成功,code不是成功code)
 *
 * @author xushuai
 * @date   2022/9/2-14:35
 * @email  466911254@qq.com
 */
class ServiceException(errorCode: Int, errorMessage: String?) :
    ApiException(errorCode, errorMessage) {
    companion object {
        private const val serialVersionUID = -3914373L
    }
}