package com.github.xs93.framework.core.crash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Process
import com.github.xs93.framework.core.activity.ActivityStackManager
import com.github.xs93.utils.ktx.appVersionCode
import com.github.xs93.utils.ktx.appVersionName
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess

/**
 * 崩溃日志收集处理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/6/13 10:42
 * @email 466911254@qq.com
 */
@SuppressLint("StaticFieldLeak")
object CrashHandler : Thread.UncaughtExceptionHandler {

    /**
     * 默认的保存日志文件文件夹名称
     */
    private const val DEFAULT_PARENT_DIR_NAME = "CrashLogs"

    private val mDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }


    /**
     * Context 上下文对象
     */
    private lateinit var mContext: Context

    /**
     * Crash日志文件头信息，包含发生Crash设备的基本信息
     */
    private var mCrashHead: String? = null

    /**
     * 默认的Crash处理方式
     */
    private var DEFAULT_HANDLER: Thread.UncaughtExceptionHandler? = null

    /**
     * 线程池,使用newSingleThreadExecutor,这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去
     */
    private val mExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }


    fun init(application: Application) {
        mContext = application
        DEFAULT_HANDLER = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        //线程中执行收集日志处理
        mExecutor.execute {
            val date = Date()
            val fileName = "${mDateFormat.format(date)}.txt"
            if (mCrashHead == null) {
                mCrashHead = createLogHead(mContext)
            }

            val lowFilePath =
                mContext.filesDir.absolutePath + File.separator + DEFAULT_PARENT_DIR_NAME
            val parentFile =
                if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                    var file = mContext.getExternalFilesDir(DEFAULT_PARENT_DIR_NAME)
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
                    pw.write(mCrashHead!!)
                    e.printStackTrace(pw)
                    var cause = e.cause
                    while (cause != null) {
                        cause.printStackTrace(pw)
                        cause = cause.cause
                    }
                }
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        }
        //结束栈内所有的activity，防止程序自动重启
        ActivityStackManager.finishAllActivity()
        //先使用默认的异常处理机制，否则直接杀死进程
        DEFAULT_HANDLER?.uncaughtException(t, e) ?: kotlin.run {
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
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