package com.github.xs93.wan.common.viewmodel

import android.annotation.SuppressLint
import com.github.xs93.core.ktx.launcherIO
import com.github.xs93.core.utils.net.KNetwork
import com.github.xs93.ui.base.viewmodel.BaseViewModel
import com.github.xs93.ui.base.viewmodel.IUiAction
import com.github.xs93.ui.base.viewmodel.IUiState
import com.github.xs93.ui.base.viewmodel.mviActions
import com.github.xs93.ui.base.viewmodel.mviStates
import com.github.xs93.wan.common.model.PageStatus
import com.github.xs93.wan.data.entity.Navigation
import com.github.xs93.wan.data.respotory.NavigatorRepository
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


    @SuppressLint("MissingPermission")
    private fun loadPageData() {
        launcherIO {

            uiState.update { copy(pageStatus = PageStatus.Loading) }

            if (!KNetwork.isNetworkConnected()) {
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