package com.github.xs93.framework.network.exception

/**
 *
 *
 *
 * @author xushuai
 * @date   2022/9/2-15:06
 * @email  466911254@qq.com
 */
class ErrorCode {
    companion object {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1002

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 协议出错,请求返回code不为200
         */
        const val HTTP_ERROR = 1003

        /**
         * 请求成功,返回的数据状态不对
         */
        const val API_ERROR = 1006
    }
}