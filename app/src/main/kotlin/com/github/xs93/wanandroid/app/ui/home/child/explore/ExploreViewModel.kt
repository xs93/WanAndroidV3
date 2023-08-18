package com.github.xs93.wanandroid.app.ui.home.child.explore

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
class ExploreViewModel @Inject constructor() :
    BaseViewModel<ExploreUiAction, ExploreUiState, ExploreUiEvent>() {

    @Inject
    lateinit var homeRepository: HomeRepository

    override fun initUiState(): ExploreUiState {
        return ExploreUiState.Init
    }

    override fun handleAction(action: ExploreUiAction) {
        when (action) {
            ExploreUiAction.InitBannerData -> {
                getBanner()
                getArticle()
            }
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

    private fun getArticle() {
        launcher {
            val bannerResponse = safeRequestApi {
                homeRepository.getHomeArticle(0)
            }
            Logger.d(bannerResponse)
            val articles = bannerResponse?.data?.datas ?: return@launcher
            setUiState {
                copy(articles = articles)
            }
        }
    }
}