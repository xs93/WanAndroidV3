package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.chad.library.adapter.base.loadState.LoadState
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
    val articleState: ArticleState
) : IUIState {
    companion object {
        val Init = ExploreUiState(PageLoadStatus.Loading, emptyList(), ArticleState.Init)
    }
}

data class ArticleState(
    val articles: List<Article>,
    val loadState: LoadState
) {
    companion object {
        val Init = ArticleState(emptyList(), LoadState.None)
    }
}

sealed class ExploreUiEvent : IUiEvent

sealed class ExploreUiAction : IUiAction {

    /**
     * 初始化页面数据
     */
    object InitPageData : ExploreUiAction()

    /**
     * 刷新文章数据
     */
    object RefreshArticleData : ExploreUiAction()

    /**
     * 加载更多文字数据
     */
    object LoadMoreArticleData : ExploreUiAction()
}