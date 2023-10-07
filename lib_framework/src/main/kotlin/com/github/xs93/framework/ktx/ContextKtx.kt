package com.github.xs93.framework.ktx

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

/**
 * Context相关扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 15:19
 * @email 466911254@qq.com
 */


inline fun <reified T : Activity> Context.startActivity(
    noinline block: ((Intent) -> Unit)? = null,
    options: Bundle? = null
) {
    val intent = Intent(this, T::class.java)
    block?.invoke(intent)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent, options)
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


fun Context.sendEmail(
    email: String,
    emailCC: Array<String>?,
    subject: String,
    content: String,
    tipsTitle: String
) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
            if (!emailCC.isNullOrEmpty()) {
                putExtra(Intent.EXTRA_CC, emailCC)
            }
        }
        startActivity(Intent.createChooser(intent, tipsTitle))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.sendEmailBySendTo(
    email: String,
    emailCC: Array<String>?,
    subject: String,
    content: String,
    tipsTitle: String
) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            if (!emailCC.isNullOrEmpty()) {
                putExtra(Intent.EXTRA_CC, emailCC)
            }
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
    } catch (e: Exception) {
        e.printStackTrace()
    }
}