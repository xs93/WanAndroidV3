package com.github.xs93.wanandroid.app.ui.home

import com.github.xs93.framework.core.base.viewmodel.BaseViewModel
import com.github.xs93.framework.core.ktx.launcher
import com.github.xs93.wanandroid.app.repository.HomeRepository

/**
 * 首页ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:16
 * @email 466911254@qq.com
 */
class HomeViewModel : BaseViewModel<HomeUiIntent, HomeUiState, HomeUiEvent>() {

    override fun initUiState(): HomeUiState {
        return HomeUiState(emptyList())
    }

    override fun handleIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.InitBannerData -> getBanner()
        }
    }

    private fun getBanner() {
        launcher {
            val bannerResponse = safeRequestApi {
                HomeRepository.getHomeBanner()
            }
            val banners = bannerResponse?.data ?: return@launcher
            setUiState {
                copy(banners = banners)
            }
        }
    }
}