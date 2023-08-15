package com.github.xs93.wanandroid.app.ui.home

import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.wanandroid.app.entity.Banner

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class HomeUiState(val banners: List<Banner>) : IUIState

sealed class HomeUiEvent : IUiEvent

sealed class HomeUiAction : IUiAction {
    object InitBannerData : HomeUiAction()
}