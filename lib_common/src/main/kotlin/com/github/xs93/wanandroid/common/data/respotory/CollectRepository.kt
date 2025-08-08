package com.github.xs93.wanandroid.common.data.respotory

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.common.data.services.CollectService
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
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