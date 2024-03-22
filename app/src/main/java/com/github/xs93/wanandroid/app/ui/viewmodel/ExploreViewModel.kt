package com.github.xs93.wanandroid.app.ui.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wanandroid.app.entity.BannerData
import com.github.xs93.wanandroid.app.repository.ExploreRepository
import com.github.xs93.wanandroid.common.entity.Article
import com.orhanobut.logger.Logger

/**
 * Explore 的ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 14:28
 * @email 466911254@qq.com
 *
 */


data class ExploreUiState(
    val banners: List<BannerData> = emptyList(),
    val articles: List<Article> = emptyList(),
    val isRefreshing: Boolean = false,
    val refreshFinished: Boolean = false
) : IUIState

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

        launcher {
            val articleResult = exploreRepository.getHomeArticles(0)
            articleResult
                .onFailure {

                }
                .onSuccess {
                    val pageData = it.data
                    if (pageData != null) {
                        val articleData = pageData.datas
                        Logger.d(articleData)
                        uiState.updateState {
                            copy(articles = articleData)
                        }
                    }
                }
        }
    }


    private suspend fun loadArticleData(refresh: Boolean) {

    }
}