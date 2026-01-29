@file:Suppress("unused")

package com.github.xs93.utils.net

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.time.debounce

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/1/22 10:55
 * @description
 *
 */
@SuppressLint("StaticFieldLeak")
object KNetwork {

    val isConnectedFlow: Flow<Boolean> by lazy {
        networksFlow.map { it.isNotEmpty() }.distinctUntilChanged()
    }

    /**当前网络状态*/
    val networkFlow: Flow<NetworkState>
        get() = _networkConnectivity.networkFlow

    /**所有网络状态*/
    val networksFlow: Flow<List<NetworkState>>
        get() = _networksConnectivity.networksFlow

    @Volatile
    private var _context: Context? = null

    private val _connectivityManager by lazy {
        val context = _context ?: error("You should call KNetwork.init() before this.")
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _networkConnectivity by lazy { NetworkConnectivity(_connectivityManager) }
    private val _networksConnectivity by lazy { NetworksConnectivity(_connectivityManager) }

    /**
     * 初始化
     */
    @JvmStatic
    fun init(context: Context) {
        context.applicationContext?.also { appContext ->
            _context = appContext
        }
    }

    /** 当前网络状态 */
    @JvmStatic
    fun currentNetworkState(): NetworkState {
        return _connectivityManager.currentNetworkState() ?: NetworkStateNone
    }

    /** 当前网络是否已连接 */
    @JvmStatic
    fun isNetworkConnected(): Boolean {
        return currentNetworkState().isConnected
    }

    /** 如果当前网络未连接，则挂起直到网络连接 */
    suspend fun awaitNetworkConnected() {
        isConnectedFlow.first { it }
    }

    /** 如果当前网络不满足[condition]，则挂起直到满足[condition] */
    suspend fun awaitNetwork(condition: (NetworkState) -> Boolean) {
        networkFlow.first { condition(it) }
    }
}

/** 如果无网络则[debounce]超时[timeoutMillis]毫秒 */
@OptIn(FlowPreview::class)
fun Flow<NetworkState>.debounceNoneNetwork(timeoutMillis: Long = 500): Flow<NetworkState> {
    return debounce {
        if (it == NetworkStateNone) timeoutMillis else 0
    }.distinctUntilChanged()
}