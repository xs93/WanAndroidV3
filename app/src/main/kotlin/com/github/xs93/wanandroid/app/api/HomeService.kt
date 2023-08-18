package com.github.xs93.wanandroid.app.api

import com.github.xs93.network.annotation.Cache
import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.app.entity.ProjectTree
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResp
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 首页使用相关API
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:05
 * @email 466911254@qq.com
 */
interface HomeService {

    /**
     * 首页Banner
     * @return WanResponse<List<Banner>>
     */
    @Cache(60 * 60 * 1000L)
    @GET("banner/json")
    suspend fun getHomeBanner(): WanResponse<List<Banner>>


    /**
     * 首页文章列表
     * @return WanResponse<PageResp<Article>> 数据对象, 分页加载数据
     *
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticle(@Path("page") page: Int): WanResponse<PageResp<Article>>

    /**
     * 项目分类接口
     * @return WanResponse<List<ProjectTree>>
     */
    @GET("project/tree/json")
    suspend fun getProjectTree(): WanResponse<List<ProjectTree>>
}