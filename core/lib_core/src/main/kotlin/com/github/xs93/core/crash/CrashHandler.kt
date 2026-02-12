package com.github.xs93.core.crash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import com.github.xs93.core.ktx.appVersionCode
import com.github.xs93.core.ktx.appVersionName
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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


    fun init(application: Application) {
        context = application
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(t, e) && defaultHandler != null) {
            defaultHandler?.uncaughtException(t, e)
            return
        } else {
            // 没有则直接杀死进程
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }

    /**
     * 处理异常
     * @return true：后续处理不需要交给系统处理 false：后续处理交给系统处理
     */
    open fun handleException(t: Thread, e: Throwable?): Boolean {
        e?.let {
            saveInfoToFile(it)
        }
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


    private fun saveInfoToFile(throwable: Throwable) {
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
                throwable.printStackTrace(pw)
                var cause = throwable.cause
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