package com.github.xs93.wanandroid.web

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.core.text.htmlEncode
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.github.xs93.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.core.ktx.dp
import com.github.xs93.core.ktx.getColorCompat
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.web.databinding.WebActivityAgentWebBinding
import com.github.xs93.wanandroid.web.webclient.WebClientFactory
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient

/**
 * WebView显示界面
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/4 12:00
 * @email 466911254@qq.com
 */
@Route(path = RouterPath.Web.WebActivity)
class WebActivity : BaseVbActivity<WebActivityAgentWebBinding>(R.layout.web_activity_agent_web) {

    @Autowired
    @JvmField
    var title: String = ""

    @Autowired
    @JvmField
    var url: String = ""


    private lateinit var mAgentWeb: AgentWeb

    override fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)

        binding.apply {
            listener = Listener()
            toolbar.title = title
        }

        val webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                binding.toolbar.title = title.htmlEncode()
            }
        }

        val layoutParams = FrameLayout.LayoutParams(-1, -1)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.flWebContainer, layoutParams)
            .useDefaultIndicator(getColorCompat(com.github.xs93.wanandroid.common.R.color.primaryColor), 1.dp(this))
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(WebClientFactory.create(url))
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(url)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!mAgentWeb.back()) {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }

    inner class Listener {
        @Suppress("DEPRECATION")
        fun clickBack() {
            this@WebActivity.onBackPressed()
        }
    }
}