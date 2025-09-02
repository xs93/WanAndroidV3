package com.github.xs93.network.exception


/**
 * 对应HTTP的状态码
 */
enum class ERROR(val code: Int, val errMsg: String) {


    /**
     * 请求错误
     */
    HTTP_BAD_REQUEST(400, "请求错误"),

    /**
     * 当前请求需要用户验证
     */
    HTTP_UNAUTHORIZED(401, "当前请求需要用户验证"),

    /**
     * 资源不可用。服务器理解客户的请求，但拒绝处理它
     */
    HTTP_FORBIDDEN(403, "资源不可用"),

    /**
     * 无法找到指定位置的资源
     */
    HTTP_NOT_FOUND(404, "无法找到指定位置的资源"),

    /**
     * 在服务器许可的等待时间内，客户一直没有发出任何请求
     */
    HTTP_REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 服务器遇到了意料不到的情况，不能完成客户的请求
     */
    HTTP_INTERNAL_SERVER_ERROR(500, "服务器错误"),

    /**
     * 服务器作为网关或者代理时，为了完成请求访问下一个服务器，但该服务器返回了非法的应答
     */
    HTTP_BAD_GATEWAY(502, "非法应答"),

    /**
     * 服务器由于维护或者负载过重未能应答
     */
    HTTP_SERVICE_UNAVAILABLE(503, "服务器未能应答"),

    /**
     * 由作为代理或网关的服务器使用，表示不能及时地从远程服务器获得应答
     */
    HTTP_GATEWAY_TIMEOUT(504, "服务器未能应答"),

    /**
     * 网络未知错误
     */
    NETWORK_ERROR_UNKNOWN(2000, "网络请求出现未知错误"),

    /**
     * 网络无连接
     */
    NETWORK_ERROR_NOT(2001, "当前无网络,请检查网络设置"),

    /**
     * 网络连接失败
     * [java.net.ConnectException]
     */
    NETWORK_ERROR_CONNECT(2002, "网络连接失败"),

    /**
     * 网络请求出现SSL错误
     * [javax.net.ssl.SSLHandshakeException]
     */
    NETWORK_ERROR_SSL(2003, "网络请求出现SSL错误"),

    /**
     * 网络请求超时
     * [java.net.SocketTimeoutException]
     */
    NETWORK_ERROR_TIMEOUT(2004, "网络请求超时"),

    /**
     * 网络请求出现未知Host错误
     * [java.net.UnknownHostException]
     */
    NETWORK_ERROR_UNKNOWN_HOST(2005, "未知Host"),

    /**
     * 数据解析错误
     */
    PARSE_ERROR(3000, "解析错误"),
}