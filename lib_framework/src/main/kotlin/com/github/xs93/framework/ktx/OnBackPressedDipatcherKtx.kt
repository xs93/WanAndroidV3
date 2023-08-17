package com.github.xs93.framework.ktx

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 16:15
 * @email 466911254@qq.com
 */


inline fun ComponentActivity.addOnBackPressedCallback(
    enable: Boolean,
    crossinline block: () -> Unit
): OnBackPressedCallback {
    val callback = object : OnBackPressedCallback(enable) {
        override fun handleOnBackPressed() {
            block()
        }
    }
    onBackPressedDispatcher.addCallback(this, callback)
    return callback
}

inline fun Fragment.addOnBackPressedCallback(
    enable: Boolean,
    crossinline block: () -> Unit
): OnBackPressedCallback {
    val callback = object : OnBackPressedCallback(enable) {
        override fun handleOnBackPressed() {
            block()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    return callback
}