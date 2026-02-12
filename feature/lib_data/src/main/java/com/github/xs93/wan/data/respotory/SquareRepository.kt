package com.github.xs93.wan.data.respotory

import com.github.xs93.wan.data.entity.Article
import com.github.xs93.wan.data.model.PageResponse
import com.github.xs93.wan.data.model.WanResponse
import com.github.xs93.wan.data.services.SquareService
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