package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.model.PageLoadStatus

/**
 * 首页浏览状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class ExploreUiState(
    val pageLoadStatus: PageLoadStatus,
    val banners: List<Banner>,
    val articles: List<Article>
) : IUIState {
    companion object {
        val Init = ExploreUiState(PageLoadStatus.Loading, emptyList(), emptyList())
    }
}

data class ArticleState(
    val articles: List<Article>,
    val finishRefresh: Boolean,
    val finishLoadMore: Boolean,
    val requestSuccess: Boolean,
    val noMoreData: Boolean
) {
    companion object {
        val Init = ArticleState(
            emptyList(),
            finishRefresh = false,
            finishLoadMore = false,
            requestSuccess = true,
            noMoreData = false,
        )
    }
}

sealed class ExploreUiEvent : IUiEvent {
    data class RequestArticleDataComplete(
        val finishRefresh: Boolean,
        val finishLoadMore: Boolean,
        val requestSuccess: Boolean,
        val noMoreData: Boolean
    ) : ExploreUiEvent()
}

sealed class ExploreUiAction : IUiAction {

    /**
     * 初始化页面数据
     */
    object InitPageData : ExploreUiAction()

    /**
     * 请求文字数据
     * @param refreshData Boolean 刷新数据
     * @constructor
     */
    data class RequestArticleData(val refreshData: Boolean) : ExploreUiAction()
}