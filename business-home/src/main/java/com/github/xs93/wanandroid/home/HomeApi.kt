package com.github.xs93.wanandroid.home

import com.github.xs93.wanandroid.common.model.Article
import com.github.xs93.wanandroid.common.model.PageDataInfo
import com.github.xs93.wanandroid.common.model.WanResponse
import com.github.xs93.wanandroid.home.model.Banner
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:11
 * @email 466911254@qq.com
 */
interface HomeApi {


    @GET("banner/json")
    suspend fun remoteBanner(): WanResponse<List<Banner>>

    @GET("article/list/{page}/json")
    suspend fun remoteHomeArticle(@Path("page") curPage: Int): WanResponse<PageDataInfo<Article>>
}