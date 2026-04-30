package com.github.xs93.wan.data.api

import com.github.xs93.wan.model.entity.Article
import com.github.xs93.wan.model.model.PageResponse
import com.github.xs93.wan.model.model.WanResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 收藏相关接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 10:13
 * @email 466911254@qq.com
 */
interface CollectApi {

    /**
     * 收藏文章列表
     * @param page Int 页面,从0开始
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): WanResponse<PageResponse<Article>>


    /**
     * 收藏站内文章
     * @param articleId Int 文章id
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") articleId: Int): WanResponse<Int>

    /**
     * 取消收藏站内文章
     * @param articleId Int 站内文章Id
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") articleId: Int): WanResponse<Int>
}