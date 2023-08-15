package com.github.xs93.utils.net

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

/**
 * 手机网络相关方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/18 9:08
 * @email 466911254@qq.com
 */

/**
 * 网络可访问
 *
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val cm = ContextCompat.getSystemService(this, ConnectivityManager::class.java) ?: return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        // 只是判断有网络连接
        cm.activeNetworkInfo?.isConnected == true
    }
}

/**
 * 网络类型
 */
@SuppressLint("MissingPermission")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getNetworkType(): NetworkType {
    val cm = ContextCompat.getSystemService(this, ConnectivityManager::class.java)
        ?: return NetworkType.NETWORK_UNKNOWN

    val isWiFi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    } else {
        cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }
    if (isWiFi) {
        return NetworkType.NETWORK_WIFI
    }

    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return when (tm.networkType) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_GSM,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G

        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType.NETWORK_3G

        TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G

        TelephonyManager.NETWORK_TYPE_IWLAN -> NetworkType.NETWORK_WIFI
        TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G

        else -> NetworkType.NETWORK_UNKNOWN
    }
}

/**
 * 获取运营商名称
 */
fun Context.getNetworkOperatorName(): String {
    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.simOperatorName
}

/**
 * <4G网络的网络判定为弱网
 */
@SuppressLint("MissingPermission")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.isWeakNetwork(): Boolean {
    if (!isNetworkConnected()) {
        return true
    }
    val type = getNetworkType()
    return type != NetworkType.NETWORK_WIFI && type != NetworkType.NETWORK_4G && type != NetworkType.NETWORK_5G
}

enum class NetworkType {
    NETWORK_UNKNOWN, NETWORK_WIFI, NETWORK_2G, NETWORK_3G, NETWORK_4G, NETWORK_5G
}