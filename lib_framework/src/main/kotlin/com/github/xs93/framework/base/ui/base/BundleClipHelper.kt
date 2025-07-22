package com.github.xs93.framework.base.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/5/29 11:08
 * @description 裁剪fragment onSaveInstanceState 保存数据,防止fragment过多导致的崩溃问题
 *
 *  实现原理参照：https://niorgai.github.io/2024/05/01/TransactionTooLargeException/Fix-Android-TransactionTooLargeException/
 *
 *  处理裁剪Bundle存储数据也可以考虑使用 android:saveEnabled="false" 关闭ViewPager2的保存功能
 *
 */
class BundleClipHelper {
    companion object {
        private const val TAG = "BundleClipHelper"

        /**
         * Bundle 中这两个 key 比较占内存, 暂时只删除这两个 key
         */
        private const val BUNDLE_KEY_FRAGMENTS = "android:support:fragments"
        private const val BUNDLE_KEY_VIEWS = "android:view_state"
    }

    private val pendingClearBundleList = mutableListOf<Bundle>()

    private val fragmentLifeCycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentSaveInstanceState(
            fm: FragmentManager,
            f: Fragment,
            outState: Bundle
        ) {
            super.onFragmentSaveInstanceState(fm, f, outState)
            pendingClearBundleList.add(outState)
        }
    }

    fun register(activity: FragmentActivity) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentLifeCycleCallbacks,
            true
        )
    }

    fun unRegister(activity: FragmentActivity) {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
            fragmentLifeCycleCallbacks
        )
        pendingClearBundleList.clear()
    }

    fun clipBundle() {
        if (pendingClearBundleList.isNotEmpty()) {
            pendingClearBundleList.forEach {
                it.remove(BUNDLE_KEY_FRAGMENTS)
                it.remove(BUNDLE_KEY_VIEWS)
            }
            pendingClearBundleList.clear()
        }
    }
}