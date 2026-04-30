package com.github.xs93.wan.data.respotory

import com.github.xs93.core.ktx.runSuspendCatching
import com.github.xs93.wan.data.api.CollectApi
import com.github.xs93.wan.model.entity.Article
import com.github.xs93.wan.model.model.PageResponse
import com.github.xs93.wan.model.model.WanResponse
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 收藏相关数据仓库
 *
 */
class CollectRepository @Inject constructor(private val collectApi: CollectApi) {

    suspend fun getCollectList(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return runSuspendCatching { collectApi.getCollectList(page) }
    }

    suspend fun collectArticle(articleId: Int): Result<WanResponse<Int>> {
        return runSuspendCatching { collectApi.collectArticle(articleId) }
    }

    suspend fun unCollectArticle(articleId: Int): Result<WanResponse<Int>> {
        return runSuspendCatching { collectApi.unCollectArticle(articleId) }
    }

    /**
     * 收藏或者取消收藏 站内文章
     * @param collect Boolean 是否收藏,true 收藏,false 取消收藏
     * @param id Int 文章id
     */
    suspend fun collectOrNotArticle(collect: Boolean, id: Int) =
        if (collect) collectArticle(id) else unCollectArticle(id)
}