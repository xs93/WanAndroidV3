package com.github.xs93.wanandroid.app.ui.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ktx.addOnBackPressedCallback
import com.github.xs93.framework.ktx.isLightStatusBarsCompat
import com.github.xs93.framework.ktx.isStatusBarTranslucentCompat
import com.github.xs93.utils.ktx.isNightMode
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ArticleWebActivityBinding
import com.github.xs93.wanandroid.common.web.WebViewPool
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/16 9:31
 * @email 466911254@qq.com
 */

@AndroidEntryPoint
class ArticleWebActivity : BaseViewBindingActivity<ArticleWebActivityBinding>(
    R.layout.article_web_activity,
    ArticleWebActivityBinding::bind
) {

    companion object {

        private const val PARAMS_URL = "params_url"

        @JvmStatic
        fun start(context: Context, url: String) {
            val starter = Intent(context, ArticleWebActivity::class.java)
                .putExtra(PARAMS_URL, url)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var mWebViewPool: WebViewPool

    private lateinit var mWebView: WebView


    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        window.apply {
            isStatusBarTranslucentCompat = true
            isLightStatusBarsCompat = isNightMode
        }

        mWebView = mWebViewPool.acquireWebView(this)
        with(mWebView) {
            isNestedScrollingEnabled = true

            with(settings) {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.progressIndicator.progress = newProgress
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    return false
                }
            }
        }

        binding.apply {
            with(flWebContainer) {
                val layoutParams = FrameLayout.LayoutParams(-1, -1)
                addView(mWebView, layoutParams)
            }

            with(toolbar) {
                setNavigationOnClickListener {
                    clickBack()
                }
            }
        }

        addOnBackPressedCallback(true) {
            clickBack()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val url = intent.getStringExtra(PARAMS_URL)
        url?.let {
            mWebView.loadUrl(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebViewPool.releaseWebView(mWebView)
    }

    private fun clickBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        } else {
            finish()
        }
    }
}