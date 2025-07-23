package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.CollectService
import javax.inject.Inject

/**
 *
 * CollectService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 10:24
 * @email 466911254@qq.com
 */
class CollectServiceImpl @Inject constructor() : CollectService {

    private val service by lazy { EasyRetrofit.create(service = CollectService::class.java) }

    override suspend fun getCollectList(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return service.getCollectList(page)
    }

    override suspend fun collectArticle(articleId: Int): Result<WanResponse<Int>> {
        return service.collectArticle(articleId)
    }

    override suspend fun unCollectArticle(articleId: Int): Result<WanResponse<Int>> {
        return service.unCollectArticle(articleId)
    }
}