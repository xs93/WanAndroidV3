package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.wanandroid.app.entity.Banner
import com.github.xs93.wanandroid.common.entity.Article

/**
 * 首页浏览状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class ExploreUiState(val banners: List<Banner>, val articles: List<Article>) : IUIState {
    companion object {
        val Init = ExploreUiState(emptyList(), emptyList())
    }
}

sealed class ExploreUiEvent : IUiEvent

sealed class ExploreUiAction : IUiAction {
    object InitBannerData : ExploreUiAction()
}