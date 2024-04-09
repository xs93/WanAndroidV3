package com.github.xs93.wanandroid.app.ui.home.child.explore

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcherIO
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.common.data.AccountDataModule
import com.github.xs93.wanandroid.common.data.CollectDataModel
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.entity.Banner
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.model.ListState
import com.github.xs93.wanandroid.common.model.ListUiState
import com.github.xs93.wanandroid.common.model.ListUpdateDataMethod
import com.github.xs93.wanandroid.common.model.PageStatus
import com.github.xs93.wanandroid.common.services.HomeService
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

    /**
     * 收藏或者取消收藏文章
     * @param collectEvent CollectEvent
     * @constructor
     */
    data class CollectArticle(val collectEvent: CollectEvent) : ExploreUiAction()
}

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val homeService: HomeService,
    private val collectDataModel: CollectDataModel,
    private val accountDataModule: AccountDataModule
) : BaseViewModel() {

    private val uiState by mviStates(ExploreUiState())
    val uiStateFlow by lazy { uiState.uiStateFlow }

    val uiAction by mviActions<ExploreUiAction> {
        when (it) {
            ExploreUiAction.InitPageData -> loadPageData()
            is ExploreUiAction.RequestArticleData -> requestArticleData(it.refreshData)
            is ExploreUiAction.CollectArticle -> collectArticle(it.collectEvent)
        }
    }


    init {
        launcherIO {
            collectDataModel.collectArticleEventFlow.collect { event ->
                val listState = uiStateFlow.value.articlesListState
                val articleList = listState.data
                val newList = articleList.map {
                    if (it.id == event.id) {
                        it.copy(collect = event.collect)
                    } else {
                        it
                    }
                }
                val newListState = listState.copy(
                    data = newList,
                    updateDataMethod = ListUpdateDataMethod.Update(false)
                )
                uiState.updateState {
                    copy(articlesListState = newListState)
                }
            }
        }
        launcherIO {
            accountDataModule.userDetailFlow.map { it.userInfo.collectIds }
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
                    val newListState = listState.copy(
                        data = newList,
                        updateDataMethod = ListUpdateDataMethod.Update(false)
                    )
                    uiState.updateState {
                        copy(articlesListState = newListState)
                    }
                }
        }
    }

    private fun loadPageData() {
        launcherIO {
            uiState.updateState { copy(pageStatus = PageStatus.Loading) }

            if (!AppInject.getApp().isNetworkConnected()) {
                uiState.updateState { copy(pageStatus = PageStatus.NoNetwork) }
                return@launcherIO
            }

            val bannerDeferred = async {
                val bannerResponse = homeService.getHomeBanner().getOrElse {
                    Logger.e(it, "请求接口失败")
                    return@async false
                }
                val banners = bannerResponse.data

                if (bannerResponse.isFailed() || banners == null) {
                    return@async false
                }

                uiState.updateState { copy(banners = banners) }
                return@async true
            }

            val articleDeferred = async {
                return@async realRequestArticleData(0, true)
            }

            val bannerSuccess = bannerDeferred.await()
            val articlesSuccess = articleDeferred.await()
            uiState.updateState {
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

            var tempListState =
                listState.copy(listUiState = if (refresh) ListUiState.Refreshing else ListUiState.LoadMore)
            uiState.updateState { copy(articlesListState = tempListState) }

            val articlesResponse = homeService.getHomeArticle(page).getOrElse {
                tempListState = tempListState.copy(
                    listUiState = if (refresh) {
                        ListUiState.RefreshFinished(false, it)
                    } else {
                        ListUiState.LoadMoreFinished(false, it)
                    }
                )
                uiState.updateState { copy(articlesListState = tempListState) }
                Logger.e(it, "请求接口失败")
                return@withContext false
            }
            val pageResp = articlesResponse.data
            if (pageResp == null) {
                tempListState = tempListState.copy(
                    listUiState = if (refresh) {
                        ListUiState.RefreshFinished(false, Throwable(articlesResponse.errorMessage))
                    } else {
                        ListUiState.LoadMoreFinished(false, Throwable(articlesResponse.errorMessage))
                    }
                )
                uiState.updateState { copy(articlesListState = tempListState) }
                return@withContext false
            }

            val newData = pageResp.datas.toMutableList().apply {
                if (!refresh) {
                    addAll(0, listState.data)
                }
            }

            val resetMethod = if (refresh) ListUpdateDataMethod.Reset else ListUpdateDataMethod.Update()
            tempListState = tempListState.copy(
                listUiState = if (refresh) {
                    ListUiState.RefreshFinished(false, null)
                } else {
                    ListUiState.LoadMoreFinished(false, null)
                }, data = newData, updateDataMethod = resetMethod, curPage = page, noMoreData = pageResp.noMoreData
            )
            uiState.updateState { copy(articlesListState = tempListState) }
            return@withContext true
        }
    }

    private fun collectArticle(collectEvent: CollectEvent) {
        launcherIO {
            collectDataModel.articleCollectAction(collectEvent)
        }
    }
}