package com.github.xs93.wanandroid.web.webclient

import android.content.Context
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.github.xs93.wanandroid.web.kts.String
import com.github.xs93.wanandroid.web.utils.Wget
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

/**
 * 专门拦截简书的WebClient
 * 参考文章：https://mp.weixin.qq.com/s/gs2bojFLBB4IAWMyN9lfnw
 * @author XuShuai
 * @version v1.0
 * @date 2021/3/15
 */
class JianShuWebClient : BaseWebClient() {

    private val rex = "(<style data-vue-ssr-id=[\\s\\S]*?>)([\\s\\S]*]?)(</style>)"
    private val bodyRex = "<body class=\"([\\ss\\S]*?)\""
    private fun darkBody(res: String): String {
        val pattern = Pattern.compile(bodyRex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val s = "<body class=\"reader-night-mode normal-size\""
            res.replace(bodyRex.toRegex(), s)
        } else res
    }

    private fun replaceCss(res: String, context: Context): String {
        val pattern = Pattern.compile(rex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val css = context.assets.open("jianshu/jianshu.css").String()
            val sb = StringBuilder()
            sb.append(m.group(1))
            sb.append(css)
            sb.append(m.group(3))
            val result = res.replace(rex.toRegex(), sb.toString())
            result
        } else {
            res
        }
    }

    @Deprecated("Deprecated in Java")
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        val urlStr = url ?: ""
        if (urlStr.startsWith(WebClientFactory.JIAN_SHU)) {
            val response = Wget.get(url ?: "")
            val res = darkBody(replaceCss(response, view!!.context))
            val input = ByteArrayInputStream(res.toByteArray())
            return WebResourceResponse("text/html", "utf-8", input)
        }
        return super.shouldInterceptRequest(view, url)
    }

}