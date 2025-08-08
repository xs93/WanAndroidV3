package com.github.xs93.wanandroid.app.ui.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiState
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcherIO
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.common.account.AccountDataManager
import com.github.xs93.wanandroid.common.data.respotory.SquareRepository
import com.github.xs93.wanandroid.common.data.usercase.CollectUserCase
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.model.ListState
import com.github.xs93.wanandroid.common.model.ListUiState
import com.github.xs93.wanandroid.common.model.PageStatus
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
    private val squareRepository: SquareRepository,
    private val collectUserCase: CollectUserCase,
    private val accountDataManager: AccountDataManager
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
            var tempListState = listState.copy(listUiState = ListUiState.RequestStart(refresh))
            uiState.update { copy(articlesListState = tempListState) }

            var requestSuccess = false

            squareRepository.getSquareArticleList(page, null)
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