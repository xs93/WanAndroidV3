package com.github.xs93.framework.core.loading.impl

import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.core.loading.ILoadingDialog
import com.github.xs93.framework.core.loading.dialog.RealLoadingDialog

/**
 * 默认LoadingDialog
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/29 11:21
 * @email 466911254@qq.com
 */
class DefaultLoadingDialog : ILoadingDialog {
    override fun createLoadingDialog(): DialogFragment {
        return RealLoadingDialog.newInstance()
    }

    override fun updateLoadingDialog(dialogFragment: DialogFragment, message: CharSequence?) {

    }
}