package com.github.xs93.wanandroid.common.data.services

import com.github.xs93.wanandroid.common.entity.Navigation
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.GET

/**
 * 导航
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 16:57
 * @email 466911254@qq.com
 */
interface NavigatorService {

    /**
     * 获取导航数据
     * @return Result<List<Navigation>>
     */
    @GET("navi/json")
    suspend fun getNavigationList(): Result<WanResponse<List<Navigation>>>
}