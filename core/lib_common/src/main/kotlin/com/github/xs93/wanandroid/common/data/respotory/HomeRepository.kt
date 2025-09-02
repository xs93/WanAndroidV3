package com.github.xs93.wanandroid.common.data.respotory

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.common.data.services.HomeService
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.entity.Banner
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 首页数据仓库
 *
 */
class HomeRepository @Inject constructor(private val homeService: HomeService) : BaseRepository() {

    suspend fun getHomeBanner(): Result<WanResponse<List<Banner>>> {
        return homeService.getHomeBanner()
    }

    suspend fun getHomeArticle(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return homeService.getHomeArticle(page)
    }
}