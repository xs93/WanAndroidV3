package com.github.xs93.framework.core.monitor

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 页面监听,当一段时间后没有操作，则触发事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/27 17:35
 */
class PageMonitor(private val time: Long, private val listener: OnPageMonitorListener? = null) :
    LifecycleEventObserver {

    private var mTag = PageMonitor::class.java.simpleName
    private val mHandler = Handler(Looper.getMainLooper())


    private val mBlackList = arrayListOf<String>()
    private var mActivity: AppCompatActivity? = null
    private var mFragment: Fragment? = null
    private var mResume = false

    private val mRunnable = Runnable { listener?.onNotTouch() }

    fun attach(activity: AppCompatActivity, tag: String = activity.javaClass.simpleName) {
        with(activity) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                inject(this)
                mActivity = this
            }
        }
    }

    fun attach(fragment: Fragment, tag: String = fragment.javaClass.simpleName) {
        with(fragment) {
            mTag = tag
            if (!mBlackList.contains(mTag)) {
                lifecycle.addObserver(this@PageMonitor)
                activity?.let {
                    inject(it)
                }
                mFragment = this
            }
        }
    }

    private fun detach() {
        mActivity?.lifecycle?.removeObserver(this)
        mFragment?.lifecycle?.removeObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_RESUME -> {
                mResume = true
                sendMessage()
            }

            Lifecycle.Event.ON_PAUSE -> {
                mResume = false
                clearMessage()
            }

            Lifecycle.Event.ON_DESTROY -> {
                detach()
            }

            else -> {

            }
        }
    }

    fun clearMessage() {
        mHandler.removeCallbacksAndMessages(null)
    }

    fun sendMessage() {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed(mRunnable, time)
    }


    /** 使用动态代理的形式监听界面的 触摸操作事件*/
    private fun inject(activity: Activity) {
        val window = activity.window
        val callback = window.callback
        val handler = WindowCallbackInvocation(callback)
        val proxy: Window.Callback = Proxy.newProxyInstance(
            Window.Callback::class.java.classLoader,
            arrayOf(Window.Callback::class.java), handler
        ) as Window.Callback
        window.callback = proxy
    }

    inner class WindowCallbackInvocation(val callback: Any) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            try {
                if ("dispatchTouchEvent" == method?.name) {
                    val event: MotionEvent = args?.get(0) as MotionEvent
                    if (MotionEvent.ACTION_DOWN == event.action) {
                        clearMessage()
                        listener?.onTouch()
                    }
                    if (MotionEvent.ACTION_UP == event.action) {
                        if (mResume) {
                            sendMessage()
                        }
                    }
                }
                return method?.invoke(callback, *(args ?: arrayOfNulls<Any>(0)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

    interface OnPageMonitorListener {
        fun onNotTouch()

        fun onTouch()
    }
}
