package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.common.entity.Navigation
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.NavigatorService
import javax.inject.Inject

/**
 * NavigatorService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 16:58
 * @email 466911254@qq.com
 */
class NavigatorServiceImpl @Inject constructor() : NavigatorService {

    private val service by lazy { EasyRetrofit.create(AppConstant.BaseUrl, service = NavigatorService::class.java) }

    override suspend fun getNavigationList(): Result<WanResponse<List<Navigation>>> {
        return service.getNavigationList()
    }
}