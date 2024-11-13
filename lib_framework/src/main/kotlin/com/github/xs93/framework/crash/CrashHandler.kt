package com.github.xs93.framework.crash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import com.github.xs93.framework.activity.ActivityStackManager
import com.github.xs93.utils.ktx.appVersionCode
import com.github.xs93.utils.ktx.appVersionName
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess

open class CrashHandler : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "CrashHandler"

        /**
         * 默认的保存日志文件文件夹名称
         */
        private const val DEFAULT_PARENT_DIR_NAME = "CrashInfo"
    }

    private val dateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    /**
     * Context 上下文对象
     */
    protected lateinit var context: Context

    /**
     * Crash日志文件头信息，包含发生Crash设备的基本信息
     */
    protected var crashHeadInfo: String? = null

    /**
     * 默认的Crash处理方式
     */
    protected var defaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 线程池,使用newSingleThreadExecutor,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去
     */
    protected val executor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    /**
     * 是否保存Crash日志
     */
    protected var saveErrorInfo: Boolean = false

    /**
     * 重启的Activity
     */
    protected var restartClass: Class<*>? = null

    fun init(application: Application, saveErrorInfo: Boolean) {
        context = application
        this.saveErrorInfo = saveErrorInfo
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    fun init(application: Application, saveErrorInfo: Boolean, restartClass: Class<*>) {
        this.restartClass = restartClass
        init(application, saveErrorInfo)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        // 线程中执行收集日志处理
        if (saveErrorInfo) {
            executor.execute {
                val date = Date()
                val fileName = "${dateFormat.format(date)}.txt"
                if (crashHeadInfo == null) {
                    crashHeadInfo = createLogHead(context)
                }

                val lowFilePath =
                    context.filesDir.absolutePath + File.separator + DEFAULT_PARENT_DIR_NAME
                val parentFile =
                    if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                        var file = context.getExternalFilesDir(DEFAULT_PARENT_DIR_NAME)
                        if (file == null) {
                            file = File(lowFilePath)
                        }
                        file
                    } else {
                        File(lowFilePath)
                    }
                if (!parentFile.exists()) {
                    parentFile.mkdirs()
                }
                val crashFile = File(parentFile, fileName)
                try {
                    PrintWriter(FileWriter(crashFile, false)).use { pw ->
                        pw.write(crashHeadInfo!!)
                        e.printStackTrace(pw)
                        var cause = e.cause
                        while (cause != null) {
                            cause.printStackTrace(pw)
                            cause = cause.cause
                        }
                    }
                } catch (e1: IOException) {
                    Log.e(TAG, "Save Error Logger to file Error", e1)
                }
            }
        }
        if (!handlerException(t, e)) {
            // 结束栈内所有的activity，防止程序自动重启
            ActivityStackManager.finishAllActivity()
            if (restartClass != null) {
                val intent = Intent(context, restartClass)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            } else {
                // 直接杀死进程
                executor.execute {
                    // 默认处理优先处理,必须保留这一项，否则不会打印系统错误日志和其他第三方处理（bugly)上报问题
                    if (defaultHandler != null) {
                        defaultHandler?.uncaughtException(t, e)
                        Process.killProcess(Process.myPid())
                        exitProcess(0)
                    } else {
                        // 没有则直接杀死进程
                        Process.killProcess(Process.myPid())
                        exitProcess(0)
                    }
                }
            }
        }
    }

    open fun handlerException(t: Thread, e: Throwable?): Boolean {
        return false
    }

    /**
     * 创建日志文件头部信息：包含手机设备信息
     *
     *
     * created at 2018/6/13 10:56
     *
     * @param context 上下文对象
     *
     * @return java.lang.String
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun createLogHead(context: Context): String {
        val versionName: String = context.appVersionName
        val versionCode: Long = context.appVersionCode
        val abis: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            @Suppress("DEPRECATION")
            arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
        }
        val abiStr = StringBuilder()
        for (abi in abis) {
            abiStr.append(abi)
            abiStr.append(',')
        }
        val cpuAbi = abiStr.toString()

        return """
             ************Crash Log Head************
             Device Manufacturer : ${Build.MANUFACTURER}
             Brand               : ${Build.BRAND} 
             Device Model        : ${Build.MODEL}
             Android Version     : ${Build.VERSION.RELEASE}_${Build.VERSION.SDK_INT}
             CPU ABI             : $cpuAbi
             App versionCode     : $versionCode
             App VersionName     : $versionName
             ************Crash Log Head************
           
             """.trimIndent()
    }
}