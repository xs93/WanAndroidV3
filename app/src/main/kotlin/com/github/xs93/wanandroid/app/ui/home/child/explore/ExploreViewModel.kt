package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.network.base.viewmodel.safeRequestApi
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.app.repository.HomeRepository
import com.github.xs93.wanandroid.common.model.PageLoadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
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

    private var mCurPage = 0

    override fun initUiState(): ExploreUiState {
        return ExploreUiState.Init
    }

    override fun handleAction(action: ExploreUiAction) {
        when (action) {
            ExploreUiAction.InitPageData -> loadPageData()
            is ExploreUiAction.RequestArticleData -> requestArticleData(action.refreshData)
        }
    }


    private fun loadPageData() {
        launcher(Dispatchers.IO) {
            setUiState {
                copy(pageLoadStatus = PageLoadStatus.Loading)
            }

            if (!AppInject.getApp().isNetworkConnected()) {
                setUiState {
                    copy(pageLoadStatus = PageLoadStatus.NoNetwork)
                }
                return@launcher
            }

            val bannerDeferred = async {
                val bannerResponse = safeRequestApi(errorBlock = null) {
                    homeRepository.getHomeBanner()
                }
                val banners = bannerResponse?.data

                if (bannerResponse == null || bannerResponse.isFailed() || banners == null) {
                    return@async false
                }

                setUiState {
                    copy(banners = banners)
                }
                return@async true
            }

            val articleDeferred = async {
                return@async realRequestArticleData(true)
            }

            val bannerSuccess = bannerDeferred.await()
            val articlesSuccess = articleDeferred.await()
            if (bannerSuccess && articlesSuccess) {
                setUiState {
                    copy(pageLoadStatus = PageLoadStatus.Success)
                }
            } else {
                setUiState {
                    copy(pageLoadStatus = PageLoadStatus.Failed)
                }
            }
        }
    }

    private fun requestArticleData(refreshData: Boolean) {
        launcher {
            realRequestArticleData(refreshData)
        }
    }

    private suspend fun realRequestArticleData(refresh: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val oldData = uiStateFlow.value.articles
            val nextPage = if (refresh) 0 else {
                mCurPage + 1
            }
            val articlesResponse = safeRequestApi {
                homeRepository.getHomeArticle(nextPage)
            }
            val pageResp = articlesResponse?.data
            if (articlesResponse == null || pageResp == null) {
                val event = ExploreUiEvent.RequestArticleDataComplete(
                    finishRefresh = refresh,
                    finishLoadMore = !refresh,
                    requestSuccess = false,
                    noMoreData = true
                )
                sendUiEvent(event)
                return@withContext false
            }

            val newData = pageResp.datas.toMutableList().apply {
                if (!refresh) {
                    addAll(0, oldData)
                }
            }

            setUiState {
                copy(articles = newData)
            }

            val loadMoreEnd = pageResp.curPage == pageResp.pageCount
            val event = ExploreUiEvent.RequestArticleDataComplete(
                finishRefresh = refresh,
                finishLoadMore = !refresh,
                requestSuccess = true,
                noMoreData = loadMoreEnd
            )
            sendUiEvent(event)
            mCurPage = nextPage
            return@withContext true
        }
    }
}