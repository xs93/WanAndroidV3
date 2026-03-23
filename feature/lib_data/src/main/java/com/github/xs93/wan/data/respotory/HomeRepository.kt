package com.github.xs93.wan.data.respotory

import com.github.xs93.core.ktx.runSuspendCatching
import com.github.xs93.wan.data.api.HomeApi
import com.github.xs93.wan.data.entity.Article
import com.github.xs93.wan.data.entity.Banner
import com.github.xs93.wan.data.model.PageResponse
import com.github.xs93.wan.data.model.WanResponse
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 首页数据仓库
 *
 */
class HomeRepository @Inject constructor(private val homeApi: HomeApi) {

    suspend fun getHomeBanner(): Result<WanResponse<List<Banner>>> {
        return runSuspendCatching { homeApi.getHomeBanner() }
    }

    suspend fun getHomeArticle(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return runSuspendCatching { homeApi.getHomeArticle(page) }
    }
}