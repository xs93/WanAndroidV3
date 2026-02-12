package com.github.xs93.wan.data.respotory

import com.github.xs93.wan.data.entity.Article
import com.github.xs93.wan.data.model.PageResponse
import com.github.xs93.wan.data.model.WanResponse
import com.github.xs93.wan.data.services.WenDaService
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