package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.WenDaService
import javax.inject.Inject

/**
 * WenDaService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 13:53
 * @email 466911254@qq.com
 */
class WenDaServiceImpl @Inject constructor() : WenDaService {

    private val service by lazy { EasyRetrofit.create(service = WenDaService::class.java) }

    override suspend fun getWenDaList(
        page: Int,
        pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>> {
        return service.getWenDaList(page, pageSize)
    }
}