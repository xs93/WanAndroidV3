package com.github.xs93.wan.data.api

import com.github.xs93.wan.model.entity.Article
import com.github.xs93.wan.model.model.PageResponse
import com.github.xs93.wan.model.model.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 问答接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 13:51
 * @email 466911254@qq.com
 */
interface WenDaApi {

    /**
     * 获取问答数据
     * @param page Int  页码,从1开始
     * @param pageSize Int? 该接口支持传入 page_size 控制分页数量，取值为[1-40]，不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     */
    @GET("/wenda/list/{page}/json ")
    suspend fun getWenDaList(
        @Path("page") page: Int,
        @Query("page_Size") pageSize: Int?
    ): WanResponse<PageResponse<Article>>
}