package com.github.xs93.ui.base.ui.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.xs93.ui.base.ui.base.interfaces.IActivity
import com.github.xs93.ui.base.ui.base.interfaces.IWindowInsetsListener


/**
 * 基础activity类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:01
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, IWindowInsetsListener {

    private val windowInsetsHelper = WindowInsetsHelper()


    override fun onCreate(savedInstanceState: Bundle?) {
        onPreCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setupEnableEdgeToEdge()
        onPreSetContentView(savedInstanceState)
        if (contentLayoutId != 0) {
            setContentView(contentLayoutId)
        } else {
            customSetContentView(savedInstanceState)
        }
        val decorView = window.decorView
        windowInsetsHelper.attach(decorView, this)

        initParams(intent, false)
        initView(savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        initParams(intent, true)
    }


    //region EdgeToEdge样式设置
    protected open fun setupEnableEdgeToEdge() {
        val statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        val navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle, navigationBarStyle)
    }
    //endregion
}