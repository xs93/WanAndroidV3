package com.github.xs93.wanandroid.common.interfaces

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/26 17:33
 * @email 466911254@qq.com
 */
interface ILoadingDialog {

    fun registerLoadingDialog(manager: FragmentManager, lifecycleOwner: LifecycleOwner)

    fun showLoading()

    fun hideLoading()
}