package com.github.xs93.wanandroid.common.interfaces.impl

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.xs93.core.ktx.isShowing
import com.github.xs93.wanandroid.common.interfaces.ILoadingDialog
import com.github.xs93.wanandroid.common.ui.dialog.LoadingDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/26 17:35
 * @email 466911254@qq.com
 */
open class LoadingDialogImpl : ILoadingDialog {

    private val loadingFlow = MutableSharedFlow<Boolean>()
    private lateinit var mManager: FragmentManager
    private lateinit var lifecycleOwner: LifecycleOwner
    private var mLoadingDialog: LoadingDialog? = null

    override fun registerLoadingDialog(manager: FragmentManager, lifecycleOwner: LifecycleOwner) {
        this.mManager = manager
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loadingFlow.collect {
                    if (it) {
                        if (mLoadingDialog.isShowing()) {
                            return@collect
                        }
                        mLoadingDialog = LoadingDialog.newInstance().apply {
                            showAllowingStateLoss(mManager, "mLoadingDialog")
                        }
                    } else {
                        mLoadingDialog?.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    override fun showLoading() {
        lifecycleOwner.lifecycleScope.launch {
            loadingFlow.emit(true)
        }
    }

    override fun hideLoading() {
        lifecycleOwner.lifecycleScope.launch {
            loadingFlow.emit(false)
        }
    }

    open fun realShowDialog() {

    }
}