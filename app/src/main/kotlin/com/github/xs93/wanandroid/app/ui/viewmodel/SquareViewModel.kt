package com.github.xs93.wanandroid.app.ui.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiState
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcherIO
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.common.data.AccountDataModule
import com.github.xs93.wanandroid.common.data.CollectDataModel
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.model.ListState
import com.github.xs93.wanandroid.common.model.ListUiState
import com.github.xs93.wanandroid.common.model.ListUpdateDataMethod
import com.github.xs93.wanandroid.common.model.PageStatus
import com.github.xs93.wanandroid.common.services.SquareService
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/5 11:18
 * @email 466911254@qq.com
 */

data class SquareUiState(
    val pageStatus: PageStatus = PageStatus.Loading,
    val articlesListState: ListState<Article> = ListState()
) : IUiState

sealed class SquareUiAction : IUiAction {

    /**
     * 初始化页面数据
     */
    data object InitPageData : SquareUiAction()

    /**
     * 请求文字数据
     * @param refreshData Boolean 刷新数据
     * @constructor
     */
    data class RequestArticleData(val refreshData: Boolean) : SquareUiAction()

    /**
     * 收藏或者取消收藏文章
     * @param collectEvent CollectEvent
     * @constructor
     */
    data class CollectArticle(val collectEvent: CollectEvent) : SquareUiAction()
}

@HiltViewModel
class SquareViewModel @Inject constructor(
    private val squareService: SquareService,
    private val collectDataModel: CollectDataModel,
    private val accountDataModule: AccountDataModule
) : BaseViewModel() {


    private val uiState by mviStates(SquareUiState())
    val uiStateFlow by lazy { uiState.flow }

    val uiAction by mviActions<SquareUiAction> {
        when (it) {
            SquareUiAction.InitPageData -> loadPageData()
            is SquareUiAction.RequestArticleData -> requestArticleData(it.refreshData)
            is SquareUiAction.CollectArticle -> collectArticle(it.collectEvent)
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
                uiState.update {
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
                    uiState.update {
                        copy(articlesListState = newListState)
                    }
                }
        }
    }

    private fun loadPageData() {
        launcherIO {
            uiState.update { copy(pageStatus = PageStatus.Loading) }

            if (!AppInject.getApp().isNetworkConnected()) {
                uiState.update { copy(pageStatus = PageStatus.NoNetwork) }
                return@launcherIO
            }

            val articleDeferred = async {
                return@async realRequestArticleData(0, true)
            }

            val articlesSuccess = articleDeferred.await()
            uiState.update {
                copy(
                    pageStatus = if (articlesSuccess) {
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
            uiState.update { copy(articlesListState = tempListState) }

            val articlesResponse = squareService.getSquareArticleList(page, null).getOrElse {
                tempListState = tempListState.copy(
                    listUiState = if (refresh) {
                        ListUiState.RefreshFinished(false, it)
                    } else {
                        ListUiState.LoadMoreFinished(false, it)
                    }
                )
                uiState.update { copy(articlesListState = tempListState) }
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
                uiState.update { copy(articlesListState = tempListState) }
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
            uiState.update { copy(articlesListState = tempListState) }
            return@withContext true
        }
    }

    private fun collectArticle(collectEvent: CollectEvent) {
        launcherIO {
            collectDataModel.articleCollectAction(collectEvent)
        }
    }
}