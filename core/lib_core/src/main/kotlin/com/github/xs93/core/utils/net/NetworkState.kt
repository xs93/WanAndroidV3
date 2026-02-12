package com.github.xs93.core.utils.net

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/1/22 10:50
 * @description 网络状态
 *
 */
interface NetworkState {

    /**网络状态id*/
    val id: String

    /**是否是wifi*/
    val isWifi: Boolean

    /**是否是手机网络*/
    val isCellular: Boolean

    /**是否是有线网络*/
    val isEthernet: Boolean

    /** 网络是否已连接*/
    val isConnected: Boolean
}

internal data class NetworkStateModel(
    /**网络id*/
    val netId: String,

    /**[android.net.NetworkCapabilities.TRANSPORT_WIFI]*/
    val transportWifi: Boolean,

    /**[android.net.NetworkCapabilities.TRANSPORT_CELLULAR]*/
    val transportCellular: Boolean,

    /**[android.net.NetworkCapabilities.TRANSPORT_ETHERNET]*/
    val transportEthernet: Boolean,

    /**[android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET]*/
    val netCapabilityInternet: Boolean
) : NetworkState {
    override val id: String get() = netId
    override val isWifi: Boolean get() = transportWifi
    override val isCellular: Boolean get() = transportCellular
    override val isEthernet: Boolean get() = transportEthernet
    override val isConnected: Boolean get() = netCapabilityInternet
}

internal val NetworkStateNone: NetworkState = NetworkStateModel(
    netId = "",
    transportWifi = false,
    transportCellular = false,
    transportEthernet = false,
    netCapabilityInternet = false
)