package com.github.xs93.wan.common.data.services

import com.github.xs93.wan.common.entity.Article
import com.github.xs93.wan.common.network.PageResponse
import com.github.xs93.wan.common.network.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 广场相关数据接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/5 11:22
 * @email 466911254@qq.com
 */
interface SquareService {


    /**
     * 广场列表数据
     * @param page Int  页码,从0 开始
     * @param pageSize Int 取值[1-40],不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     * @return Result<WanResponse<PageResponse<Article>>>
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticleList(
        @Path("page") page: Int,
        @Query("page_Size") pageSize: Int?
    ): Result<WanResponse<PageResponse<Article>>>
}