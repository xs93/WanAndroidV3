package com.github.xs93.wan.data.respotory

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wan.data.entity.Article
import com.github.xs93.wan.data.model.PageResponse
import com.github.xs93.wan.data.model.WanResponse
import com.github.xs93.wan.data.services.CollectService
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 收藏相关数据仓库
 *
 */
class CollectRepository @Inject constructor(private val collectService: CollectService) :
    BaseRepository() {

    suspend fun getCollectList(page: Int): Result<WanResponse<PageResponse<Article>>> {
        return collectService.getCollectList(page)
    }

    suspend fun collectArticle(articleId: Int): Result<WanResponse<Int>> {
        return collectService.collectArticle(articleId)
    }

    suspend fun unCollectArticle(articleId: Int): Result<WanResponse<Int>> {
        return collectService.unCollectArticle(articleId)
    }
}