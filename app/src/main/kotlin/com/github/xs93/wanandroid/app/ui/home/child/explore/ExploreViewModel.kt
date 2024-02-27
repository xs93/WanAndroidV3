package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.app.repository.HomeRepository
import com.github.xs93.wanandroid.common.model.PageLoadStatus
import com.orhanobut.logger.Logger
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
class ExploreViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var homeRepository: HomeRepository

    private var mCurPage = 0


    private val exploreStates by mviStates(
        ExploreUiState(
            PageLoadStatus.Loading,
            emptyList(),
            emptyList()
        )
    )
    val exploreStateFlow by lazy { exploreStates.uiStateFlow }

    private val exploreEvents by mviEvents<ExploreUiEvent>()
    val exploreEventFlow by lazy { exploreEvents.uiEventFlow }

    val exploreActions by mviActions<ExploreUiAction> {
        when (it) {
            ExploreUiAction.InitPageData -> loadPageData()
            is ExploreUiAction.RequestArticleData -> requestArticleData(it.refreshData)
        }
    }


    private fun loadPageData() {
        launcher(Dispatchers.IO) {
            exploreStates.updateState { copy(pageLoadStatus = PageLoadStatus.Loading) }

            if (!AppInject.getApp().isNetworkConnected()) {
                exploreStates.updateState { copy(pageLoadStatus = PageLoadStatus.NoNetwork) }
                return@launcher
            }

            val bannerDeferred = async {
                val bannerResponse = homeRepository.getHomeBanner().getOrElse {
                    Logger.e(it, "请求接口失败")
                    return@async false
                }
                val banners = bannerResponse.data

                if (bannerResponse.isFailed() || banners == null) {
                    return@async false
                }

                exploreStates.updateState { copy(banners = banners) }
                return@async true
            }

            val articleDeferred = async {
                return@async realRequestArticleData(true)
            }

            val bannerSuccess = bannerDeferred.await()
            val articlesSuccess = articleDeferred.await()
            if (bannerSuccess && articlesSuccess) {
                exploreStates.updateState { copy(pageLoadStatus = PageLoadStatus.Success) }
            } else {
                exploreStates.updateState { copy(pageLoadStatus = PageLoadStatus.Failed) }
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
            val oldData = exploreStates.uiStateFlow.value.articles
            val nextPage = if (refresh) 0 else {
                mCurPage + 1
            }
            val articlesResponse = homeRepository.getHomeArticle(nextPage).getOrElse {
                Logger.e(it, "请求接口失败")
                val event = ExploreUiEvent.RequestArticleDataComplete(
                    finishRefresh = refresh,
                    finishLoadMore = !refresh,
                    requestSuccess = false,
                    noMoreData = true
                )
                exploreEvents.sendEvent(event)
                return@withContext false
            }
            val pageResp = articlesResponse.data
            if (pageResp == null) {
                val event = ExploreUiEvent.RequestArticleDataComplete(
                    finishRefresh = refresh,
                    finishLoadMore = !refresh,
                    requestSuccess = false,
                    noMoreData = true
                )
                exploreEvents.sendEvent(event)
                return@withContext false
            }

            val newData = pageResp.datas.toMutableList().apply {
                if (!refresh) {
                    addAll(0, oldData)
                }
            }

            exploreStates.updateState { copy(articles = newData) }

            val loadMoreEnd = pageResp.curPage == pageResp.pageCount
            val event = ExploreUiEvent.RequestArticleDataComplete(
                finishRefresh = refresh,
                finishLoadMore = !refresh,
                requestSuccess = true,
                noMoreData = loadMoreEnd
            )
            exploreEvents.sendEvent(event)
            mCurPage = nextPage
            return@withContext true
        }
    }
}