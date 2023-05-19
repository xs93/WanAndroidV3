package com.github.xs93.framework.core.loading

import androidx.fragment.app.DialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/15 17:12
 * @email 466911254@qq.com
 */
object LoadingDialogHelper {

    private var mILoadingDialog: ILoadingDialog? = null

    fun initLoadingDialog(loadingDialog: ILoadingDialog) {
        mILoadingDialog = loadingDialog
    }

    fun createLoadingDialog(): DialogFragment {
        return mILoadingDialog?.createLoadingDialog()
            ?: throw IllegalArgumentException("please call LoadingDialogHelper.initLoadingDialog")
    }

    fun updateLoadingDialog(dialogFragment: DialogFragment, message: CharSequence?) {
        mILoadingDialog?.updateLoadingDialog(dialogFragment, message)
    }
}