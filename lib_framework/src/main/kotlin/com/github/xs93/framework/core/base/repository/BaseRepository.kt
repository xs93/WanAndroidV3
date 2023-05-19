package com.github.xs93.framework.core.base.repository

/**
 * 基础数据仓库类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/5/17 17:34
 */
open class BaseRepository {

    protected suspend fun <T> request(block: suspend () -> T?): T? {
        val result = kotlin.runCatching {
            block()
        }.onFailure {
            it.printStackTrace()
        }
        return result.getOrNull()
    }
}