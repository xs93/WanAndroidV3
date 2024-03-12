package com.github.xs93.wanandroid.app.ui.mvi

import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.wanandroid.app.entity.BannerData
import com.github.xs93.wanandroid.common.entity.Article

/**
 * 首页浏览状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class ExploreUiState(
    val banners: List<BannerData> = emptyList(),
    val articles: List<Article> = emptyList()
) : IUIState

sealed class ExploreUiAction : IUiAction {

    /**
     * 初始化页面数据
     */
    data object InitPageData : ExploreUiAction()

    /**
     * 请求文字数据
     * @param refreshData Boolean 刷新数据
     * @constructor
     */
    data class RequestArticleData(val refreshData: Boolean) : ExploreUiAction()
}