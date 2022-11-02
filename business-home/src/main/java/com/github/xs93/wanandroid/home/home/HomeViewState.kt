package com.github.xs93.wanandroid.home.home

import com.github.xs93.wanandroid.common.model.Article
import com.github.xs93.wanandroid.home.model.Banner

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:24
 * @email 466911254@qq.com
 */
data class HomeViewState(val banners: List<Banner>, val articles: List<Article>)


sealed class HomeIntent {
    object StartInitIntent : HomeIntent()
    object LoadMoreArticle : HomeIntent()
}

sealed class HomeEvent {
    object StartRefreshEvent : HomeEvent()
    data class FinishRefreshEvent(val success: Boolean) : HomeEvent()
    object StartLoadMoreEvent : HomeEvent()
    data class FinishLoadMoreEvent(val success: Boolean) : HomeEvent()
}