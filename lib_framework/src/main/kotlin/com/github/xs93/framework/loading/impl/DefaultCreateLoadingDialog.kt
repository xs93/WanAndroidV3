package com.github.xs93.framework.loading.impl

import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.dialog.RealLoadingDialog

/**
 * 默认LoadingDialog
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/29 11:21
 * @email 466911254@qq.com
 */
class DefaultCreateLoadingDialog : ICreateLoadingDialog {
    override fun createLoadingDialog(): DialogFragment {
        return RealLoadingDialog.newInstance()
    }
}