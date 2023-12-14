package com.github.xs93.utils.ktx

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils

/**
 * Context 扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/15 9:42
 * @email 466911254@qq.com
 */

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


fun Context.jumpToGoogleRating(id: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=${id}")
            `package` = "com.android.vending"
        }
        startActivity(intent)
    } catch (e: Exception) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${id}")
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
            data = Uri.parse(dataString.toString())
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
        val uri = Uri.parse(url)
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