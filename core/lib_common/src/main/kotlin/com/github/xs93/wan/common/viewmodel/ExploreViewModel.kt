package com.github.xs93.wan.common.viewmodel

import android.annotation.SuppressLint
import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiState
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcherIO
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wan.common.account.AccountDataManager
import com.github.xs93.wan.common.data.respotory.HomeRepository
import com.github.xs93.wan.common.data.usercase.CollectUserCase
import com.github.xs93.wan.common.entity.Article
import com.github.xs93.wan.common.entity.Banner
import com.github.xs93.wan.common.model.CollectEvent
import com.github.xs93.wan.common.model.ListState
import com.github.xs93.wan.common.model.ListUiState
import com.github.xs93.wan.common.model.PageStatus
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

data class ExploreUiState(
    val pageStatus: PageStatus = PageStatus.Loading,
    val banners: List<Banner> = listOf(),
    val articlesListState: ListState<Article> = ListState()
) : IUiState

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

    /**
     * 收藏或者取消收藏文章
     * @param collectEvent CollectEvent
     * @constructor
     */
    data class CollectArticle(val collectEvent: CollectEvent) : ExploreUiAction()
}

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val collectUserCase: CollectUserCase,
    private val accountDataManager: AccountDataManager
) : BaseViewModel() {

    private val uiState by mviStates(ExploreUiState())
    val uiStateFlow by lazy { uiState.flow }

    val uiAction by mviActions<ExploreUiAction> {
        when (it) {
            ExploreUiAction.InitPageData -> loadPageData()
            is ExploreUiAction.RequestArticleData -> requestArticleData(it.refreshData)
            is ExploreUiAction.CollectArticle -> collectArticle(it.collectEvent)
        }
    }


    init {
        launcherIO {
            collectUserCase.collectArticleEventFlow.collect { event ->
                val listState = uiStateFlow.value.articlesListState
                val articleList = listState.data
                val newList = articleList.map {
                    if (it.id == event.id) {
                        it.copy(collect = event.collect)
                    } else {
                        it
                    }
                }
                val newListState = listState.copy(data = newList)
                uiState.update {
                    copy(articlesListState = newListState)
                }
            }
        }
        launcherIO {
            accountDataManager.userDetailFlow.map { it.userInfo.collectIds }
                .distinctUntilChanged()
                .collect { collectIds ->
                    val listState = uiStateFlow.value.articlesListState
                    val articleList = listState.data
                    val newList = articleList.map {
                        if (collectIds.contains(it.id)) {
                            if (!it.collect) {
                                it.copy(collect = true)
                            } else {
                                it
                            }
                        } else {
                            if (it.collect) {
                                it.copy(collect = false)
                            } else {
                                it
                            }
                        }
                    }
                    val newListState = listState.copy(data = newList)
                    uiState.update {
                        copy(articlesListState = newListState)
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadPageData() {
        launcherIO {
            uiState.update { copy(pageStatus = PageStatus.Loading) }

            if (!AppInject.getApp().isNetworkConnected()) {
                uiState.update { copy(pageStatus = PageStatus.NoNetwork) }
                return@launcherIO
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

                uiState.update { copy(banners = banners) }
                return@async true
            }

            val articleDeferred = async {
                return@async realRequestArticleData(0, true)
            }

            val bannerSuccess = bannerDeferred.await()
            val articlesSuccess = articleDeferred.await()
            uiState.update {
                copy(
                    pageStatus = if (bannerSuccess && articlesSuccess) {
                        PageStatus.Success
                    } else {
                        PageStatus.Failed
                    }
                )
            }
        }
    }

    private fun requestArticleData(refreshData: Boolean) {
        launcherIO {
            if (refreshData) {
                realRequestArticleData(0, true)
            } else {
                val curPage = uiStateFlow.value.articlesListState.curPage
                realRequestArticleData(curPage + 1, false)
            }
        }
    }

    private suspend fun realRequestArticleData(page: Int, refresh: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val listState = uiStateFlow.value.articlesListState

            var tempListState = listState.copy(listUiState = ListUiState.RequestStart(refresh))
            uiState.update { copy(articlesListState = tempListState) }

            var requestSuccess = false

            homeRepository.getHomeArticle(page)
                .onSuccess {
                    val pageResp = it.data
                    if (pageResp == null) {
                        tempListState = tempListState.copy(
                            listUiState = ListUiState.RequestFinishFailed(
                                refresh,
                                Throwable(it.errorMessage)
                            )
                        )
                        uiState.update { copy(articlesListState = tempListState) }
                        requestSuccess = false
                    } else {
                        val newData = pageResp.datas.toMutableList().apply {
                            if (!refresh) {
                                addAll(0, listState.data)
                            }
                        }

                        tempListState = tempListState.copy(
                            listUiState = ListUiState.RequestFinish(refresh, pageResp.noMoreData),
                            data = newData,
                            curPage = page
                        )
                        uiState.update { copy(articlesListState = tempListState) }
                        requestSuccess = true
                    }
                }
                .onFailure {
                    tempListState = tempListState.copy(
                        listUiState = ListUiState.RequestFinishFailed(refresh, Throwable(it))
                    )
                    uiState.update { copy(articlesListState = tempListState) }
                    requestSuccess = false
                }
            return@withContext requestSuccess
        }
    }

    private fun collectArticle(collectEvent: CollectEvent) {
        launcherIO {
            collectUserCase.articleCollectAction(collectEvent)
        }
    }
}