package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.entity.Banner
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.HomeService
import javax.inject.Inject

/**
 * HomeService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 15:58
 * @email 466911254@qq.com
 */
class HomeServiceImpl @Inject constructor() : HomeService {

    private val service by lazy { EasyRetrofit.create(AppConstant.BaseUrl, service = HomeService::class.java) }

    override suspend fun getHomeBanner(): Result<WanResponse<List<Banner>>> {
        return service.getHomeBanner()
    }

    override suspend fun getHomeArticle(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return service.getHomeArticle(page)
    }
}