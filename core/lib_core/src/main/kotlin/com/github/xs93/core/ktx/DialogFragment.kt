package com.github.xs93.core.ktx

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.lang.reflect.Field

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
    get() = (this != null && dialog != null && dialog?.isShowing == true)


/**
 * 使用此方法显示弹出框，可以避免生命周期状态错误导致的异常(Can not perform this action after onSaveInstanceState)
 * @param manager FragmentManager
 * @param tag String?
 */
fun DialogFragment.showAllowingStateLoss(
    manager: FragmentManager,
    tag: String? = this::class.java.simpleName
) {
    try {
        val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
        dismissed.isAccessible = true
        dismissed.set(this, false)
        val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
        shown.isAccessible = true
        shown.set(this, true)
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}