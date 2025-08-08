package com.github.xs93.wanandroid.app.ui.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiState
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcherIO
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
import com.github.xs93.wanandroid.common.data.respotory.NavigatorRepository
import com.github.xs93.wanandroid.common.entity.Navigation
import com.github.xs93.wanandroid.common.model.PageStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 17:00
 * @email 466911254@qq.com
 */

data class NavigatorChildUiState(
    val pageStatus: PageStatus = PageStatus.Loading,
    val navigationList: List<Navigation> = emptyList()
) : IUiState


sealed class NavigatorChildUiAction : IUiAction {

    /**
     * 初始化页面数据
     */
    data object InitPageData : NavigatorChildUiAction()
}

@HiltViewModel
class NavigatorChildViewModel @Inject constructor(private val navigatorRepository: NavigatorRepository) :
    BaseViewModel() {

    private val uiState by mviStates(NavigatorChildUiState())
    val uiStateFlow by lazy { uiState.flow }

    val uiAction by mviActions<NavigatorChildUiAction> {
        when (it) {
            NavigatorChildUiAction.InitPageData -> loadPageData()
        }
    }


    private fun loadPageData() {
        launcherIO {

            uiState.update { copy(pageStatus = PageStatus.Loading) }

            if (!AppInject.getApp().isNetworkConnected()) {
                uiState.update { copy(pageStatus = PageStatus.NoNetwork) }
                return@launcherIO
            }

            val remoteData = navigatorRepository.getNavigationList()
            remoteData
                .onSuccess {
                    if (it.isSuccess()) {
                        val data = it.data
                        if (data.isNullOrEmpty()) {
                            uiState.update {
                                copy(pageStatus = PageStatus.Empty)
                            }
                        } else {
                            uiState.update {
                                copy(pageStatus = PageStatus.Success, data)
                            }
                        }
                    } else {
                        uiState.update {
                            copy(pageStatus = PageStatus.Failed)
                        }
                    }
                }
                .onFailure {
                    uiState.update { copy(pageStatus = PageStatus.Failed) }
                }
        }
    }
}