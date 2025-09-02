package com.github.xs93.framework.loading

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.xs93.utils.ktx.isShowing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/15 17:11
 * @email 466911254@qq.com
 */
class ILoadingDialogControlProxy(
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
    private val createLoadingDialog: ICreateLoadingDialog
) : ILoadingDialogControl, ICreateLoadingDialog {

    private val mChannel = Channel<LoadingDialogUiIntent>(Channel.UNLIMITED)
    private val mFlow = mChannel.receiveAsFlow()

    private var mLoadingDialog: DialogFragment? = null

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mFlow.collect {
                    when (it) {
                        is LoadingDialogUiIntent.ShowLoadingDialogUiIntent -> {
                            if (mLoadingDialog.isShowing) {
                                return@collect
                            }
                            createLoadingDialog()
                                .also { dialog -> mLoadingDialog = dialog }
                                .show(fragmentManager, "mLoadingDialog")
                        }

                        LoadingDialogUiIntent.HideLoadingDialogUiIntent -> {
                            mLoadingDialog?.dismissAllowingStateLoss()
                        }
                    }
                }
            }
        }
    }

    override fun createLoadingDialog(): DialogFragment {
        return createLoadingDialog.createLoadingDialog()
    }

    override fun showLoadingDialog() {
        lifecycleOwner.lifecycleScope.launch {
            mChannel.send(LoadingDialogUiIntent.ShowLoadingDialogUiIntent)
        }
    }

    override fun hideLoadingDialog() {
        lifecycleOwner.lifecycleScope.launch {
            mChannel.send(LoadingDialogUiIntent.HideLoadingDialogUiIntent)
        }
    }

    private sealed class LoadingDialogUiIntent {
        data object ShowLoadingDialogUiIntent : LoadingDialogUiIntent()

        data object HideLoadingDialogUiIntent : LoadingDialogUiIntent()
    }
}