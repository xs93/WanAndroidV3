package com.github.xs93.wanandroid.app.ui.home

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.network.base.viewmodel.safeRequestApi
import com.github.xs93.wanandroid.app.repository.HomeRepository
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 首页ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:16
 * @email 466911254@qq.com
 */
@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeUiAction, HomeUiState, HomeUiEvent>() {

    @Inject
    lateinit var homeRepository: HomeRepository

    override fun initUiState(): HomeUiState {
        return HomeUiState(emptyList())
    }

    override fun handleAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.InitBannerData -> getBanner()
        }
    }

    private fun getBanner() {
        launcher {
            val bannerResponse = safeRequestApi {
                homeRepository.getHomeBanner()
            }
            Logger.d(bannerResponse)
            val banners = bannerResponse?.data ?: return@launcher
            setUiState {
                copy(banners = banners)
            }
            showToast("加载Banner成功")
        }
    }
}