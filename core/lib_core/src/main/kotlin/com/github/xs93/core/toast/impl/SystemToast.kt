package com.github.xs93.core.toast.impl

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.github.xs93.core.toast.IToast
import java.lang.reflect.Field

/**
 * 系统Toast 实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/12 14:21
 * @email 466911254@qq.com
 */
class SystemToast(context: Context) : IToast {

    private var sFieldTN: Field? = null
    private var sFieldTNHandler: Field? = null

    private val mContext: Context = context.applicationContext

    private val mMainHandler = Handler(Looper.getMainLooper())

    private var mCommonTransform: OnToastTransform? = null

    fun setCommonTransform(transform: OnToastTransform?) {
        mCommonTransform = transform
    }

    override fun showToast(charSequence: CharSequence, duration: Int) {
        mMainHandler.post {
            val toast = Toast.makeText(mContext, charSequence, duration)
            toast.setText(charSequence)
            mCommonTransform?.onTransform(toast, charSequence)
            hook(toast)
            toast.show()
        }
    }

    override fun showToast(resId: Int, vararg formatArgs: Any?, duration: Int) {
        val content = mContext.getString(resId, *formatArgs)
        showToast(content, duration)
    }

    /** 修复版本7.1.1 toast显示错误bug */
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun hook(toast: Toast?) {
        toast?.let {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    if (sFieldTN == null) {
                        sFieldTN = Toast::class.java.getDeclaredField("mTN")
                        sFieldTN?.isAccessible = true
                        sFieldTNHandler = sFieldTN?.type?.getDeclaredField("mHandler")
                        sFieldTNHandler?.isAccessible = true
                    }
                    val tn = sFieldTN!!.get(it)
                    val preHandler: Handler = sFieldTNHandler!!.get(tn) as Handler
                    sFieldTNHandler!!.set(tn, SafelyHandlerWrapper(preHandler))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    interface OnToastTransform {
        fun onTransform(toast: Toast, charSequence: CharSequence)
    }
}