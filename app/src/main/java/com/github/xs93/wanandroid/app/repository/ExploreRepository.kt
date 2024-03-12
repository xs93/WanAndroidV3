package com.github.xs93.wanandroid.app.repository

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.app.api.ExploreService
import com.github.xs93.wanandroid.app.entity.BannerData
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageData
import com.github.xs93.wanandroid.common.network.WanResponse

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/6 16:09
 * @email 466911254@qq.com
 */
class ExploreRepository : BaseRepository() {

    private val exploreService by lazy {
        EasyRetrofit.create(AppConstant.BaseUrl, ExploreService::class.java)
    }

    /**
     * 获取首页Banner数据
     * @return Result<WanResponse<List<BannerData>>>
     */
    suspend fun getHomeBanner(): Result<WanResponse<List<BannerData>>> {
        return runSafeSuspendCatching {
            exploreService.getHomeBanner()
        }
    }

    /**
     * 获取首页文章列表
     * @param page Int 请求数据的page
     * @return Result<WanResponse<PageData<Article>>>
     */
    suspend fun getHomeArticles(page: Int): Result<WanResponse<PageData<Article>>> {
        return runSafeSuspendCatching {
            exploreService.getHomeArticles(page)
        }
    }
}