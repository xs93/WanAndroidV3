package com.github.xs93.wanandroid.app.ui.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wanandroid.app.repository.ExploreRepository
import com.github.xs93.wanandroid.app.ui.mvi.ExploreUiAction
import com.github.xs93.wanandroid.app.ui.mvi.ExploreUiState
import com.orhanobut.logger.Logger

/**
 * Explore 的ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 14:28
 * @email 466911254@qq.com
 */
class ExploreViewModel : BaseViewModel() {

    private val exploreRepository by lazy { ExploreRepository() }

    private val uiState by mviStates(ExploreUiState())
    val uiStateFlow by lazy { uiState.uiStateFlow }

    val uiAction by mviActions<ExploreUiAction> {
        when (it) {
            ExploreUiAction.InitPageData -> initPageData()
            is ExploreUiAction.RequestArticleData -> {}
        }
    }

    init {
        initPageData()
    }

    private fun initPageData() {
        launcher {
            val bannerResult = exploreRepository.getHomeBanner()
            bannerResult
                .onFailure {

                }
                .onSuccess {
                    val bannerList = it.data
                    if (bannerList != null) {
                        uiState.updateState {
                            copy(banners = bannerList)
                        }
                        Logger.d(bannerList)
                    }
                }
        }
    }
}