package com.github.xs93.wanandroid.app.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.xs93.wanandroid.app.router.AppNavHost
import com.github.xs93.wanandroid.app.ui.theme.AppTheme

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/12 17:41
 * @email 466911254@qq.com
 */


@Composable
fun ArticleDetailsScreen(articleId: Long, title: String, url: String) {
    ArticleDetailsContent(articleId = articleId, title = title, url = url)
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailsContent(articleId: Long, title: String, url: String) {
    var mWebView: WebView? = null
    Scaffold(topBar = {
        Surface(shadowElevation = 8.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.github.xs93.common.R.drawable.common_ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(48.dp)
                            .padding(12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false),
                                onClick = {
                                    mWebView?.let {
                                        if (it.canGoBack()) {
                                            it.goBack()
                                        } else {
                                            AppNavHost.navController.popBackStack()
                                        }
                                    } ?: run {
                                        AppNavHost.navController.popBackStack()
                                    }
                                }
                            )
                    )
                }
            }
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val progress = remember { mutableIntStateOf(0) }
            val webLoading = remember { mutableStateOf(false) }
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory =
                if (LocalInspectionMode.current) {
                    {
                        WebView(it)
                    }
                } else {
                    {
                        val webView = WebView(it)
                        mWebView = webView
                        webView.apply {
                            with(settings) {
                                javaScriptEnabled = true
                                javaScriptCanOpenWindowsAutomatically = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                loadsImagesAutomatically = true
                                mediaPlaybackRequiresUserGesture = false
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    webLoading.value = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    webLoading.value = false
                                }
                            }
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    super.onProgressChanged(view, newProgress)
                                    progress.intValue = newProgress
                                }
                            }
                            loadUrl(url)
                        }
                        webView
                    }
                }
            )
            if (webLoading.value) {
                LinearProgressIndicator(
                    progress = { progress.intValue / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.background,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewArticleDetails() {
    AppTheme {
        ArticleDetailsContent(2L, "百度", "https://www.baidu.com")
    }
}