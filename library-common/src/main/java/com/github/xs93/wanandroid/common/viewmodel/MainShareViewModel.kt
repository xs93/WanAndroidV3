package com.github.xs93.wanandroid.common.viewmodel

import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.wanandroid.common.ktx.launcher
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 *
 * Main界面共享的ViewModel,在多模块共享一些数据
 *
 * @author xushuai
 * @date   2022/9/6-15:42
 * @email  466911254@qq.com
 */
class MainShareViewModel : BaseViewModel() {

    val drawerLayoutState: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun openDrawerLayout() {
        launcher {
            drawerLayoutState.emit(true)
        }
    }

    fun closeDrawerLayout() {
        launcher {
            drawerLayoutState.emit(false)
        }
    }
}