package com.github.xs93.wanandroid.app.api

import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.app.entity.ProjectTree
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.GET

/**
 * 首页使用相关API
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:05
 * @email 466911254@qq.com
 */
interface HomeApi {

    /**
     * 首页Banner
     * @return WanResponse<List<Banner>>
     */
    @GET("banner/json")
    suspend fun getHomeBanner(): WanResponse<List<Banner>>


    /**
     * 项目分类接口
     * @return WanResponse<List<ProjectTree>>
     */
    @GET("project/tree/json")
    suspend fun getProjectTree(): WanResponse<List<ProjectTree>>
}