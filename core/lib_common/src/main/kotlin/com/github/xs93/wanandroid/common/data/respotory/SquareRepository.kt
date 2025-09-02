package com.github.xs93.wanandroid.common.data.respotory

import com.github.xs93.wanandroid.common.data.services.SquareService
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResponse
import com.github.xs93.wanandroid.common.network.WanResponse
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/5 11:25
 * @email 466911254@qq.com
 */
class SquareRepository @Inject constructor(private val squareService: SquareService) {

    suspend fun getSquareArticleList(
        page: Int,
        pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>> {
        return squareService.getSquareArticleList(page, pageSize)
    }
}