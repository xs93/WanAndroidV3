package com.github.xs93.framework.base.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.xs93.framework.loading.ILoadingDialogControl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * BaseViewModel Event相关扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 15:36
 * @email 466911254@qq.com
 */


fun BaseViewModel.registerCommonEvent(activity: AppCompatActivity) {
    commonEventFlow.flowWithLifecycle(activity.lifecycle, Lifecycle.State.STARTED)
        .onEach {
            when (it) {
                CommonUiEvent.ShowLoadingDialog -> {
                    if (activity is ILoadingDialogControl) {
                        activity.showLoadingDialog()
                    }
                }

                CommonUiEvent.HideLoadingDialog -> {
                    if (activity is ILoadingDialogControl) {
                        activity.hideLoadingDialog()
                    }
                }
            }
        }
        .launchIn(activity.lifecycleScope)
}

fun BaseViewModel.registerCommonEvent(fragment: Fragment) {
    commonEventFlow.flowWithLifecycle(
        fragment.viewLifecycleOwner.lifecycle,
        Lifecycle.State.STARTED
    ).onEach {
        when (it) {
            CommonUiEvent.ShowLoadingDialog -> {
                if (fragment is ILoadingDialogControl) {
                    fragment.showLoadingDialog()
                }
            }

            CommonUiEvent.HideLoadingDialog -> {
                if (fragment is ILoadingDialogControl) {
                    fragment.hideLoadingDialog()
                }
            }
        }
    }.launchIn(fragment.viewLifecycleOwner.lifecycleScope)
}