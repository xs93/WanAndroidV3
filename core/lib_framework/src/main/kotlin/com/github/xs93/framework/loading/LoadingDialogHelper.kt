package com.github.xs93.framework.loading

import androidx.fragment.app.DialogFragment

/**
 * LoadingDialog 辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/15 17:12
 * @email 466911254@qq.com
 */
object LoadingDialogHelper {

    private var mICreateLoadingDialog: ICreateLoadingDialog? = null

    fun initLoadingDialog(loadingDialog: ICreateLoadingDialog) {
        mICreateLoadingDialog = loadingDialog
    }

    fun createLoadingDialog(): DialogFragment {
        return mICreateLoadingDialog?.createLoadingDialog()
            ?: throw IllegalArgumentException("please call LoadingDialogHelper.initLoadingDialog")
    }
}