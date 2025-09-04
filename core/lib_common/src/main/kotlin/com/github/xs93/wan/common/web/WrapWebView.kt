package com.github.xs93.wan.common.web

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * WebView包装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/6/3 14:50
 * @email 466911254@qq.com
 */
class WrapWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_URL = "about:blank"
    }

    override fun canGoBack(): Boolean {
        val backForwardList = copyBackForwardList()
        val currentIndex = backForwardList.currentIndex
        return if (currentIndex == 1) {
            val currentUrl = backForwardList.getItemAtIndex(0)?.url
            if (currentUrl == DEFAULT_URL) {
                return false
            }
            return super.canGoBack()
        } else {
            super.canGoBack()
        }
    }
}