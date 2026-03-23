package com.github.xs93.wan.data.respotory

import com.github.xs93.core.ktx.runSuspendCatching
import com.github.xs93.wan.data.api.SquareApi
import com.github.xs93.wan.data.entity.Article
import com.github.xs93.wan.data.model.PageResponse
import com.github.xs93.wan.data.model.WanResponse
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/5 11:25
 * @email 466911254@qq.com
 */
class SquareRepository @Inject constructor(private val squareApi: SquareApi) {

    suspend fun getSquareArticleList(
        page: Int,
        pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>> {
        return runSuspendCatching {
            squareApi.getSquareArticleList(page, pageSize)
        }
    }
}