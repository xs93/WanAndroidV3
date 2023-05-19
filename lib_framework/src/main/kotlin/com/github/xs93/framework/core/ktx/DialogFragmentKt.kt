package com.github.xs93.framework.core.ktx

import androidx.fragment.app.DialogFragment

/**
 *
 * DialogFragment相关扩展函数
 *
 * @author xushuai
 * @date   2022/5/12-23:52
 * @email  466911254@qq.com
 */

/**
 * 判断当前dialogFragment 是否显示
 * @receiver DialogFragment? 目标对象
 * @return Boolean true 显示，false不显示
 */
inline val DialogFragment?.isShowing
    get() = this != null && dialog != null && dialog?.isShowing == true
