package com.github.xs93.wanandroid.app.repository

import com.github.xs93.framework.core.base.repository.BaseRepository
import com.github.xs93.framework.network.EasyRetrofit
import com.github.xs93.wanandroid.app.api.HomeApi
import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.common.network.WanResponse

/**
 * 主页数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:03
 * @email 466911254@qq.com
 */
object HomeRepository : BaseRepository() {

    private val homeApi by lazy {
        EasyRetrofit.create(service = HomeApi::class.java)
    }

    suspend fun getHomeBanner(): WanResponse<List<Banner>>? {
        return requestApi {
            homeApi.getHomeBanner()
        }
    }
}