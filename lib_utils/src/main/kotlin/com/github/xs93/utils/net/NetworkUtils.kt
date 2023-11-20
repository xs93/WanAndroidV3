@file:Suppress("unused")

package com.github.xs93.utils.net

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

/**
 * 手机网络相关方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/18 9:08
 * @email 466911254@qq.com
 */

/**
 * 网络是否可连接
 *
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val cm = getSystemService(this, ConnectivityManager::class.java) ?: return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        // 只是判断有网络连接
        @Suppress("DEPRECATION")
        cm.activeNetworkInfo?.isConnected == true
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
 * 当前可用网络类型
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getNetworkType(): NetworkType {
    val accessNetworkStatePermission =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
    if (accessNetworkStatePermission == PackageManager.PERMISSION_DENIED) {
        return NetworkType.NETWORK_UNKNOWN
    }
    val cm = getSystemService(this, ConnectivityManager::class.java)
        ?: return NetworkType.NETWORK_UNKNOWN
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        getNetworkType(cm)
    } else {
        getNetworkType23OrNew(cm)
    }
}


/**
 * 判断当前网络类型
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getNetworkType(network: Network): NetworkType {
    val accessNetworkStatePermission =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
    if (accessNetworkStatePermission == PackageManager.PERMISSION_DENIED) {
        return NetworkType.NETWORK_UNKNOWN
    }

    val cm = getSystemService(this, ConnectivityManager::class.java)
        ?: return NetworkType.NETWORK_UNKNOWN

    val networkCapabilities = cm.getNetworkCapabilities(network)
        ?: return NetworkType.NETWORK_NONE
    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        return NetworkType.NETWORK_WIFI
    }
    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        return NetworkType.NETWORK_MOBILE
    }
    return NetworkType.NETWORK_UNKNOWN
}


/**
 * 获取具体的移动网络详细信息
 * @receiver Context
 * @return MobileNetworkType
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE])
fun Context.getMobileNetworkType(): MobileNetworkType {
    val networkType = getNetworkType()
    if (networkType != NetworkType.NETWORK_MOBILE) {
        return MobileNetworkType.NOT_MOBILE_NETWORK
    }

    val readPhoneStatePermission =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
    if (readPhoneStatePermission == PackageManager.PERMISSION_DENIED) {
        return MobileNetworkType.MOBILE_UNKNOWN
    }

    val tm = getSystemService(this, TelephonyManager::class.java)
        ?: return MobileNetworkType.MOBILE_UNKNOWN

    val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        tm.dataNetworkType
    } else {
        @Suppress("DEPRECATION")
        tm.networkType
    }

    if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
        return MobileNetworkType.MOBILE_UNKNOWN
    }


    return when (type) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_GSM,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        @Suppress("DEPRECATION")
        TelephonyManager.NETWORK_TYPE_IDEN -> MobileNetworkType.MOBILE_2G

        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> MobileNetworkType.MOBILE_3G

        TelephonyManager.NETWORK_TYPE_LTE -> MobileNetworkType.MOBILE_4G
        TelephonyManager.NETWORK_TYPE_NR -> MobileNetworkType.MOBILE_5G
        TelephonyManager.NETWORK_TYPE_IWLAN -> MobileNetworkType.NOT_MOBILE_NETWORK
        else -> MobileNetworkType.MOBILE_UNKNOWN
    }
}


@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isWifiConnected(): Boolean {
    return getNetworkType() == NetworkType.NETWORK_WIFI
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
    return if (type == NetworkType.NETWORK_MOBILE) {
        val mobileNetworkType = getMobileNetworkType()
        mobileNetworkType != MobileNetworkType.MOBILE_4G && mobileNetworkType != MobileNetworkType.MOBILE_5G
    } else {
        false
    }
}


@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
private fun getNetworkType(cm: ConnectivityManager): NetworkType {
    val networkInfo = cm.activeNetworkInfo
    if (networkInfo == null || !networkInfo.isConnectedOrConnecting) {
        return NetworkType.NETWORK_NONE
    }
    return when (networkInfo.type) {
        ConnectivityManager.TYPE_WIFI -> {
            NetworkType.NETWORK_WIFI
        }

        ConnectivityManager.TYPE_MOBILE -> {
            NetworkType.NETWORK_MOBILE
        }

        else -> {
            NetworkType.NETWORK_UNKNOWN
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
private fun getNetworkType23OrNew(cm: ConnectivityManager): NetworkType {
    val network = cm.activeNetwork
    val networkCapabilities = cm.getNetworkCapabilities(network)
    if (network == null || networkCapabilities == null) {
        return NetworkType.NETWORK_NONE
    }
    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        return NetworkType.NETWORK_WIFI
    }
    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        return NetworkType.NETWORK_MOBILE
    }

    return NetworkType.NETWORK_UNKNOWN
}

