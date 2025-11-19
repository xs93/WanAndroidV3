@file:Suppress("unused")

package com.github.xs93.framework.ktx

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/14 10:43
 * @description window相关扩展
 *
 */

//region 导航栏和状态栏图标颜色
/**
 * 修改状态栏图标是否是浅色
 */
var Window.isAppearanceLightStatusBars: Boolean
    get() = WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars
    set(value) {
        WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = value
    }

/**
 * 修改导航栏图标是否是浅色
 */
var Window.isAppearanceLightNavigationBars: Boolean
    get() = WindowCompat.getInsetsController(this, decorView).isAppearanceLightNavigationBars
    set(value) {
        WindowCompat.getInsetsController(this, decorView).isAppearanceLightNavigationBars = value
    }

var Activity.isAppearanceLightStatusBars: Boolean
    get() {
        val window = window
        if (window == null) return false
        return window.isAppearanceLightStatusBars
    }
    set(value) {
        val window = window
        if (window == null) return
        window.isAppearanceLightStatusBars = value
    }

var Activity.isAppearanceLightNavigationBars: Boolean
    get() {
        val window = window
        if (window == null) return false
        return window.isAppearanceLightNavigationBars
    }
    set(value) {
        val window = window
        if (window == null) return
        window.isAppearanceLightNavigationBars = value
    }

var DialogFragment.isAppearanceLightStatusBars: Boolean
    get() {
        val window = dialog?.window
        if (window == null) return false
        return window.isAppearanceLightStatusBars
    }
    set(value) {
        val window = dialog?.window
        if (window == null) return
        window.isAppearanceLightStatusBars = value
    }

var DialogFragment.isAppearanceLightNavigationBars: Boolean
    get() {
        val window = dialog?.window
        if (window == null) return false
        return window.isAppearanceLightNavigationBars
    }
    set(value) {
        val window = dialog?.window
        if (window == null) return
        window.isAppearanceLightNavigationBars = value
    }

var Dialog.isAppearanceLightStatusBars: Boolean
    get() {
        val window = window
        if (window == null) return false
        return window.isAppearanceLightStatusBars
    }
    set(value) {
        val window = window
        if (window == null) return
        window.isAppearanceLightStatusBars = value
    }

var Dialog.isAppearanceLightNavigationBars: Boolean
    get() {
        val window = window
        if (window == null) return false
        return window.isAppearanceLightNavigationBars
    }
    set(value) {
        val window = window
        if (window == null) return
        window.isAppearanceLightNavigationBars = value
    }
//endregion


fun Dialog.transparentBackgroundForBottomSheet() {
    setOnShowListener {
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(android.R.color.transparent)
    }
}