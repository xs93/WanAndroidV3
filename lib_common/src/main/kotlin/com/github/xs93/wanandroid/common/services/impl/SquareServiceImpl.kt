package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.SquareService
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/5 11:25
 * @email 466911254@qq.com
 */
class SquareServiceImpl @Inject constructor() : SquareService {

    private val service by lazy { EasyRetrofit.create(service = SquareService::class.java) }

    override suspend fun getSquareArticleList(
        page: Int,
        pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>> {
        return service.getSquareArticleList(page, pageSize)
    }
}