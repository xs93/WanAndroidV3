package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.chad.library.adapter.base.loadState.LoadState
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
            ExploreUiAction.RefreshArticleData -> refreshArticle()
            ExploreUiAction.LoadMoreArticleData -> loadMoreArticle()
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
                val articlesResponse = safeRequestApi(errorBlock = null) {
                    val page = 0
                    homeRepository.getHomeArticle(page)
                }
                val pageResp = articlesResponse?.data
                if (articlesResponse == null || pageResp == null) {
                    return@async false
                }

                val loadMoreEnd = pageResp.curPage == pageResp.pageCount
                val articleState = ArticleState(pageResp.datas, LoadState.NotLoading(loadMoreEnd))
                setUiState {
                    copy(articleState = articleState)
                }
                mCurPage = 0

                return@async true
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


    private fun refreshArticle() {
        launcher(Dispatchers.IO) {
            val nextPage = 0
            val articlesResponse = safeRequestApi {
                homeRepository.getHomeArticle(nextPage)
            }
            val pageResp = articlesResponse?.data
            if (articlesResponse == null || pageResp == null) {
                return@launcher
            }

            val loadMoreEnd = pageResp.curPage == pageResp.pageCount
            val articleState = ArticleState(pageResp.datas, LoadState.NotLoading(loadMoreEnd))
            setUiState {
                copy(articleState = articleState)
            }
            mCurPage = nextPage
        }
    }


    private fun loadMoreArticle() {
        launcher(Dispatchers.IO) {
            val oldArticleState = uiStateFlow.value.articleState

            val nextPage = mCurPage + 1
            val articlesResponse = safeRequestApi({
                val articleState = oldArticleState.copy(loadState = LoadState.Error(it))
                setUiState {
                    copy(articleState = articleState)
                }
            }) {
                homeRepository.getHomeArticle(nextPage)
            }
            val pageResp = articlesResponse?.data
            if (articlesResponse == null || pageResp == null) {
                return@launcher
            }

            val oldData = uiStateFlow.value.articleState.articles
            val newData = oldData.toMutableList().apply {
                addAll(pageResp.datas)
            }
            val loadMoreEnd = pageResp.curPage == pageResp.pageCount
            val articleState = ArticleState(newData, LoadState.NotLoading(loadMoreEnd))
            setUiState {
                copy(articleState = articleState)
            }
            mCurPage = nextPage
        }
    }
}