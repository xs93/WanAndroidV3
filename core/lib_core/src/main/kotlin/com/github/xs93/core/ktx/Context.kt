package com.github.xs93.core.ktx

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.core.net.toUri

/**
 * Context 扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/15 9:42
 * @email 466911254@qq.com
 */

inline val Context.isDebug: Boolean
    get() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0


inline val Context.appName: String
    get() {
        var appName = ""
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            val labelRes = packageInfo.applicationInfo?.labelRes
            labelRes?.let {
                appName = resources.getString(it)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appName
    }

inline val Context.appVersionName: String
    get() {
        var versionName = ""
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            packageInfo.versionName?.let {
                versionName = it
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

inline val Context.appVersionCode: Long
    get() {
        var versionCode: Long = -1L
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

inline val Context.isNightMode: Boolean
    get() {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

inline fun <reified T : Activity> Context.startActivitySafe(
    noinline block: ((Intent) -> Unit)? = null,
    options: Bundle? = null
) {
    val intent = Intent(this, T::class.java)
    block?.invoke(intent)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        startActivity(intent, options)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


fun Context.jumpToGoogleRating(id: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "market://details?id=${id}".toUri()
            `package` = "com.android.vending"
        }
        startActivity(intent)
    } catch (e: Exception) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/apps/details?id=${id}".toUri()
                `package` = "com.android.vending"
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context.sendEmailBySendTo(
    email: Array<String>,
    emailCC: Array<String>?,
    emailBCC: Array<String>?,
    subject: String,
    content: String,
    tipsTitle: String
) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val dataString = StringBuilder("mailto:${TextUtils.join(",", email)}")
            val ccString = if (emailCC.isNullOrEmpty()) {
                ""
            } else {
                TextUtils.join(",", emailCC)
            }
            val bccString = if (emailBCC.isNullOrEmpty()) {
                ""
            } else {
                TextUtils.join(",", emailBCC)
            }
            dataString.append("?cc=$ccString")
            dataString.append("&bcc=$bccString")
            dataString.append("&subject=$subject")
            dataString.append("&body=${content}")
            data = dataString.toString().toUri()
            if (!emailCC.isNullOrEmpty()) {
                putExtra(Intent.EXTRA_CC, emailCC)
            }
            if (!emailBCC.isNullOrEmpty()) {
                putExtra(Intent.EXTRA_BCC, emailBCC)
            }
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        startActivity(Intent.createChooser(intent, tipsTitle))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openBrowser(url: String) {
    try {
        val uri = url.toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun Context.isInstall(packageName: String): Boolean {
    val pm = packageManager
    val applicationInfo: ApplicationInfo
    return try {
        applicationInfo = pm.getApplicationInfo(packageName, 0)
        applicationInfo.enabled
    } catch (e: Exception) {
        false
    }
}

fun Context.copy(content: String, label: String = "label"): Boolean {
    return try {
        val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, content)
        cm.setPrimaryClip(clipData)
        true
    } catch (e: Exception) {
        false
    }
}