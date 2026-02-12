package com.github.xs93.core.utils.listener

import android.view.View

/**
 * 防抖点击事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/25 15:29
 * @email 466911254@qq.com
 */
class SingleClickListener(
    private val interval: Long = 400,
    private val singleClick: View.OnClickListener? = null
) : View.OnClickListener {

    private var mLastClickTime = 0L
    override fun onClick(v: View) {
        if (System.currentTimeMillis() - mLastClickTime > interval) {
            singleClick?.onClick(v)
            mLastClickTime = System.currentTimeMillis()
        }
    }
}