package com.github.xs93.wanandroid.home.home

import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.ktx.launcher
import com.github.xs93.wanandroid.home.HomeRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

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

    private val _homeState = MutableStateFlow(HomeViewState(emptyList()))
    val homeState: StateFlow<HomeViewState> = _homeState

    fun input(intent: HomeIntent) {
        when (intent) {
            HomeIntent.StartInitIntent -> {
                getBanner()
            }
        }
    }


    private fun getBanner() {
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
}