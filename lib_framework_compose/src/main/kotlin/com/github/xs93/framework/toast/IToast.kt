package com.github.xs93.framework.toast

import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 基础UiToast接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/12 14:13
 * @email 466911254@qq.com
 */
interface IToast {

    fun showToast(charSequence: CharSequence, duration: Int = Toast.LENGTH_SHORT)

    fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT)
}