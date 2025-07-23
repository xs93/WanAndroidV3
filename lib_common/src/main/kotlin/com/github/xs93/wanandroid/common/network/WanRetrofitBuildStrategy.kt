package com.github.xs93.wanandroid.common.network

import com.github.xs93.network.strategy.RetrofitBuildStrategy
import okhttp3.CookieJar
import javax.inject.Inject

/**
 * retrofit 构建策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:49
 * @email 466911254@qq.com
 */
class WanRetrofitBuildStrategy @Inject constructor(private val cookieJar: CookieJar) :
    RetrofitBuildStrategy() {

    override fun getCookieJar(): CookieJar {
        return cookieJar
    }
}