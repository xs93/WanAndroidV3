package com.chatbot.bkfire.club.compose.app.ui.mvi

import com.chatbot.bkfire.club.compose.app.entity.BannerData
import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent

/**
 * 首页浏览状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class ExploreUiState(
    val banners: List<BannerData>
) : IUIState


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
    data object InitPageData : ExploreUiAction()

    /**
     * 请求文字数据
     * @param refreshData Boolean 刷新数据
     * @constructor
     */
    data class RequestArticleData(val refreshData: Boolean) : ExploreUiAction()
}