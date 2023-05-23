package com.github.xs93.wanandroid.app.ui.home

import com.github.xs93.framework.core.base.viewmodel.IUIState
import com.github.xs93.framework.core.base.viewmodel.IUiEvent
import com.github.xs93.framework.core.base.viewmodel.IUiIntent
import com.github.xs93.wanandroid.app.entity.Banner

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:42
 * @email 466911254@qq.com
 */

data class HomeUiState(val bannerUiState: BannerUiState) : IUIState

data class BannerUiState(
    val banners: List<Banner>
)

sealed class HomeUiEvent : IUiEvent

sealed class HomeUiIntent : IUiIntent {
    object InitBannerData : HomeUiIntent()
}