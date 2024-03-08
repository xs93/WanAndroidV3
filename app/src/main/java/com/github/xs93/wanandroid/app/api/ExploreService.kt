package com.github.xs93.wanandroid.app.api

import com.github.xs93.wanandroid.app.entity.BannerData
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.GET

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
}