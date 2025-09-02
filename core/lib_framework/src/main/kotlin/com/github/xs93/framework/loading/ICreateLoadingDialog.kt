package com.github.xs93.framework.loading

import androidx.fragment.app.DialogFragment

/**
 * 加载弹窗接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/15 17:45
 * @email 466911254@qq.com
 */
interface ICreateLoadingDialog {

    fun createLoadingDialog(): DialogFragment
}