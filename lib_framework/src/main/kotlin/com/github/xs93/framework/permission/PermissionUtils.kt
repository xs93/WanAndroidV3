package com.github.xs93.framework.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/6/16 10:02
 * @description 权限工具类
 *
 */
object PermissionUtils {

    /**
     * 检查权限
     */
    @JvmStatic
    fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 是否需要显示权限 rational
     */
    @JvmStatic
    fun shouldShowRationale(activity: FragmentActivity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * 打开设置界面
     */
    @JvmStatic
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        if (context !is FragmentActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * 打开通知权限界面
     */
    @JvmStatic
    fun openNotificationSettings(context: Context) {
        try {
            val applicationInfo = context.applicationInfo
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(Settings.EXTRA_APP_PACKAGE, applicationInfo.packageName)
                }
                putExtra("app_package", applicationInfo.packageName)
                putExtra("app_uid", applicationInfo.uid)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            openAppSettings(context)
        }
    }
}