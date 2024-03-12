package com.github.xs93.wanandroid.app.api

import com.github.xs93.wanandroid.app.entity.BannerData
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageData
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 首页浏览Service
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 14:33
 * @email 466911254@qq.com
 */
interface ExploreService {

    /**
     * 获取首页Banner数据
     * @return Result<WanResponse<List<BannerData>>>
     */
    @GET("banner/json")
    suspend fun getHomeBanner(): WanResponse<List<BannerData>>

    /**
     * 获取首页列表数据
     * @param page Int 分页页面,从0开始
     * @return WanResponse<List<Article>>
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): WanResponse<PageData<Article>>
}