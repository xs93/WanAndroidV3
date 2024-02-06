package com.github.xs93.framework.base.ui.function

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.github.xs93.framework.base.ui.base.BaseActivity

/**
 * 可以添加公共功能的activity
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/17 11:13
 */
abstract class BaseFunctionActivity : BaseActivity() {

    private val functions: MutableList<BaseActivityFunction> = FunctionsManager.createActivityFunctions()

    override fun onCreate(savedInstanceState: Bundle?) {
        addFunctions()
        super.onCreate(savedInstanceState)
        for (function in functions) {
            function.onCreated(this, savedInstanceState)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        for (function in functions) {
            function.onNewIntent(this, intent)
        }
    }

    override fun onStart() {
        super.onStart()
        for (function in functions) {
            function.onStarted(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        for (function in functions) {
            function.onRestarted(this)
        }
    }

    override fun onResume() {
        super.onResume()
        for (function in functions) {
            function.onResumed(this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (function in functions) {
            function.onActivityResult(this, requestCode, resultCode, data)
        }
    }


    override fun onPause() {
        super.onPause()
        for (function in functions) {
            function.onPaused(this)
        }
    }

    override fun onStop() {
        super.onStop()
        for (function in functions) {
            function.onStopped(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        for (function in functions) {
            function.onSaveInstanceState(this, outState)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        for (function in functions) {
            function.onRestoreInstanceState(this, savedInstanceState)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        for (function in functions) {
            function.onWindowFocusChanged(this, hasFocus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for (function in functions) {
            function.onDestroyed(this)
        }
    }

    override fun finish() {
        super.finish()
        for (function in functions) {
            function.onFinish(this)
        }
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        for (function in functions) {
            function.convertGetResourceResult(this, resources)
        }
        return resources
    }

    /**
     * 添加界面单独的功能
     */
    open fun addFunctions() {}

    /**
     * 添加一个functions
     * @param function BaseActivityFunction
     */
    fun addFunction(function: BaseActivityFunction) {
        functions.add(function)
    }
}