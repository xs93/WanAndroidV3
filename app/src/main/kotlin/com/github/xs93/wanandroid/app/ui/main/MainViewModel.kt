package com.github.xs93.wanandroid.app.ui.main

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 首页ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 13:54
 * @email 466911254@qq.com
 */
@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    private val mainEvents by mviEvents<MainEvent>()
    val mainEventFlow = mainEvents.uiEventFlow


    val mainActions by mviActions<MainAction> {
        when (it) {
            MainAction.OpenDrawerAction -> openDrawer()
        }
    }


    private fun openDrawer() {
        mainEvents.sendEvent(MainEvent.OpenDrawerEvent)
    }
}