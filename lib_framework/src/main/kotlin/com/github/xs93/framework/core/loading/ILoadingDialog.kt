package com.github.xs93.framework.core.loading

import androidx.fragment.app.DialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/15 17:45
 * @email 466911254@qq.com
 */
interface ILoadingDialog {

    fun createLoadingDialog(): DialogFragment

    fun updateLoadingDialog(dialogFragment: DialogFragment, message: CharSequence?)
}