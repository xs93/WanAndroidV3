package com.github.xs93.wanandroid.common.data.respotory

import com.github.xs93.wanandroid.common.data.services.WenDaService
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import javax.inject.Inject

/**
 * WenDaService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 13:53
 * @email 466911254@qq.com
 */
class WenDaRepository @Inject constructor(private val wenDaService: WenDaService) {

    suspend fun getWenDaList(
        page: Int,
        pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>> {
        return wenDaService.getWenDaList(page, pageSize)
    }
}