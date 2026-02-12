package com.github.xs93.ui.base.ui.base

import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import android.view.AbsSavedState
import androidx.core.util.forEach
import androidx.core.util.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.xs93.core.log.logD

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
        private const val BUNDLE_VIEW_HIERARCHY_STATE = "android:viewHierarchyState"
        private const val BUNDLE_VIEWS = "android:views"
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

    fun clipBundle(outState: Bundle) {
        if (pendingClearBundleList.isNotEmpty()) {
            pendingClearBundleList.forEach {
                it.remove(BUNDLE_KEY_FRAGMENTS)
                it.remove(BUNDLE_KEY_VIEWS)
            }
            pendingClearBundleList.clear()
        }
        if (outState.containsKey(BUNDLE_VIEW_HIERARCHY_STATE)) {
            val hierarchyState = outState.getBundle(BUNDLE_VIEW_HIERARCHY_STATE)
            if (hierarchyState != null) {
                val hierarchyStateSize = KB(sizeAsParcel(hierarchyState))
                logD(TAG, "hierarchyStateSize: ${hierarchyStateSize}KB")
//                logD(TAG, "hierarchyState: $hierarchyState")
//                printBundle(bundle = hierarchyState)
//                printViews(hierarchyState)
                if (hierarchyStateSize > 500f) {
                    outState.remove(BUNDLE_VIEW_HIERARCHY_STATE)
                }
            }
        }
        val bundleSize = KB(sizeAsParcel(outState))
        logD(TAG, "bundleSize: ${bundleSize}KB")
        if (bundleSize > 1000f) {
            outState.clear()
        }
    }

    // region bundle 打印辅助类

    private fun printViews(bundle: Bundle) {
        val views: SparseArray<Parcelable>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSparseParcelableArray(BUNDLE_VIEWS, AbsSavedState::class.java)
            } else {
                bundle.getSparseParcelableArray(BUNDLE_VIEWS)
            }

        if (views != null) {
            logD(TAG, "views size: ${views.size}")
            views.forEach { index, value ->
                val parcel = Parcel.obtain()
                try {
                    parcel.writeValue(value)
                    val size = KB(parcel.dataSize())
                    logD(TAG, "$index view: $value --- $size")
                } finally {
                    parcel.recycle()
                }
            }
        }
    }


    private fun printBundle(bundle: Bundle, indent: String = "") {
        val keys = bundle.keySet()  // 获取所有的键
        val sizeMap = getBundleSizeMap(bundle)
        keys.forEachIndexed { index, key ->
            val value = bundle.get(key)
            if (value is Bundle) {
                val info =
                    "${indent}key$index $key (bundle) size: ${KB(sizeMap[key] ?: 0)}KB keyCount ${value.keySet().size}"
                logD(TAG, info)
                printBundle(value, "$indent    ")  // 递归打印嵌套的Bundle，增加缩进
            } else {
                logD(TAG, "${indent}key$index $key size ${KB(sizeMap[key] ?: 0)}KB")
            }
        }
    }

    private fun getBundleSizeMap(bundle: Bundle): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        val copy = Bundle(bundle)
        try {
            var bundleSize = sizeAsParcel(bundle)
            // Iterate over copy's keys because we're removing those of the original bundle
            for (key in copy.keySet()) {
                bundle.remove(key)
                val newBundleSize = sizeAsParcel(bundle)
                val valueSize = bundleSize - newBundleSize
                map[key] = valueSize
                bundleSize = newBundleSize
            }
        } finally {
            // Put everything back into original bundle
            bundle.putAll(copy)
        }
        return map
    }

    private fun sizeAsParcel(bundle: Bundle): Int {
        val parcel = Parcel.obtain()
        try {
            parcel.writeBundle(bundle)
            return parcel.dataSize()
        } finally {
            parcel.recycle()
        }
    }

    private fun KB(bytes: Int): Float {
        return bytes.toFloat() / 1000f
    }
    // endregion
}