package com.github.xs93.wanandroid.home.home

import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.ktx.launcher
import com.github.xs93.wanandroid.common.model.Article
import com.github.xs93.wanandroid.home.HomeRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:05
 * @email 466911254@qq.com
 */
class HomeViewModel : BaseViewModel() {

    private val homeRepository: HomeRepository by lazy {
        HomeRepository()
    }

    private val _homeState = MutableStateFlow(HomeViewState(emptyList(), emptyList()))
    val homeState: StateFlow<HomeViewState> = _homeState

    private val _homeEvent = Channel<HomeEvent>()
    val homeEvent: Flow<HomeEvent> = _homeEvent.receiveAsFlow()

    private var mCurPage = 0

    fun input(intent: HomeIntent) {
        when (intent) {
            HomeIntent.StartInitIntent -> {
                getBanner()
                mCurPage = 0
                getArticle(mCurPage) {
                    if (it) {
                        mCurPage++
                    }
                }
            }
            else -> {
                getArticle(mCurPage) {
                    if (it) {
                        mCurPage++
                    }
                }
            }
        }
    }

    private fun getBanner() {
        if (_homeState.value.banners.isNotEmpty()) {
            return
        }
        launcher {
            homeRepository.getBanner().collect {
                when (it) {
                    is RequestState.Error -> {
                        Logger.e(it.exception, "load Banner failed")
                    }
                    RequestState.Loading -> {
                        Logger.d("Start load Banner")
                    }
                    is RequestState.Success -> {
                        val data = it.data
                        if (data != null) {
                            _homeState.emit(_homeState.value.copy(banners = data))
                        }
                    }
                }
            }
        }
    }

    private fun getArticle(curPage: Int, result: (Boolean) -> Unit) {
        launcher {
            homeRepository.getCurArticle(curPage).collectLatest {
                when (it) {
                    RequestState.Loading -> {
                        Logger.d("Start Load Article,curPage = $curPage")
                        if (curPage == 0) {
                            _homeEvent.send(HomeEvent.StartRefreshEvent)
                        } else {
                            _homeEvent.send(HomeEvent.StartLoadMoreEvent)
                        }
                    }

                    is RequestState.Error -> {
                        Logger.e(it.exception, "Load Article failed,curPage = $curPage")
                        result.invoke(false)
                        if (curPage == 0) {
                            _homeEvent.send(HomeEvent.FinishRefreshEvent(false))
                        } else {
                            _homeEvent.send(HomeEvent.FinishLoadMoreEvent(false))
                        }
                    }

                    is RequestState.Success -> {
                        it.data?.let { pageDataInfo ->
                            val oldData = _homeState.value.articles
                            val newData: MutableList<Article> = if (curPage == 0) {
                                pageDataInfo.datas.toMutableList()
                            } else {
                                oldData.toMutableList().apply {
                                    addAll(pageDataInfo.datas)
                                }
                            }
                            Logger.d(newData)
                            _homeState.emit(_homeState.value.copy(articles = newData))
                        }

                        result.invoke(true)
                        if (curPage == 0) {
                            _homeEvent.send(HomeEvent.FinishRefreshEvent(true))
                        } else {
                            _homeEvent.send(HomeEvent.FinishLoadMoreEvent(true))
                        }
                    }
                }
            }
        }
    }
}