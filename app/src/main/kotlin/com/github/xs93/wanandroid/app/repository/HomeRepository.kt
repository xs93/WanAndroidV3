package com.github.xs93.wanandroid.app.repository

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.app.api.HomeService
import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.network.PageResp
import com.github.xs93.wanandroid.common.network.WanResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * 主页数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:03
 * @email 466911254@qq.com
 */
@ActivityRetainedScoped
class HomeRepository @Inject constructor(private val homeService: HomeService) : BaseRepository() {

    suspend fun getHomeBanner(): WanResponse<List<Banner>>? {
        return safeRequestApi {
            homeService.getHomeBanner()
        }
    }

    /**
     * 查询文章数据
     * @param page Int 分页当前页数
     * @return WanResponse<PageResp<Article>>? 获取数据信息
     */
    suspend fun getHomeArticle(page: Int): WanResponse<PageResp<Article>>? {
        return safeRequestApi {
            homeService.getHomeArticle(page)
        }
    }
}