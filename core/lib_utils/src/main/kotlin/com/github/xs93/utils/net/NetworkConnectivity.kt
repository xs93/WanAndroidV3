package com.github.xs93.utils.net

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/1/22 11:00
 * @description 网络状态监听
 *
 */

/** 当前网络 */
internal class NetworkConnectivity(manager: ConnectivityManager) :
    BaseNetworkConnectivity(manager) {

    private val _networkFlow = MutableStateFlow<NetworkState?>(null)
    val networkFlow: Flow<NetworkState>
        get() = _networkFlow.filterNotNull()

    @SuppressLint("MissingPermission")
    override fun onRegisterCallback(manager: ConnectivityManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(this)
        } else {
            error("Android 5.0 or later is required")
        }
    }

    override fun onRegisterCallbackResult(register: Boolean, networkState: NetworkState?) {
        val state = networkState ?: NetworkStateNone
        if (register) {
            _networkFlow.compareAndSet(null, state)
        } else {
            _networkFlow.value = state
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        _networkFlow.value = newNetworkState(network, networkCapabilities)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _networkFlow.value = NetworkStateNone
    }

}

/** 所有网络 */
internal class NetworksConnectivity(manager: ConnectivityManager) :
    BaseNetworkConnectivity(manager) {
    private val _networks = mutableMapOf<Network, NetworkState>()
    private val _networksFlow = MutableStateFlow<List<NetworkState>?>(null)
    val networksFlow: Flow<List<NetworkState>>
        get() = _networksFlow.filterNotNull()

    @SuppressLint("MissingPermission")
    override fun onRegisterCallback(manager: ConnectivityManager) {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        manager.registerNetworkCallback(request, this)
    }

    override fun onRegisterCallbackResult(register: Boolean, networkState: NetworkState?) {
        val list = if (networkState != null) listOf(networkState) else emptyList()
        if (register) {
            _networksFlow.compareAndSet(null, list)
        } else {
            _networksFlow.value = list
        }
    }

    override fun onLost(network: Network) {
        _networks.remove(network)
        _networksFlow.value = _networks.values.toList()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        _networks[network] = newNetworkState(network, networkCapabilities)
        _networksFlow.value = _networks.values.toList()
    }
}

internal abstract class BaseNetworkConnectivity(val manager: ConnectivityManager) :
    ConnectivityManager.NetworkCallback() {

    @SuppressLint("MissingPermission")
    private suspend fun registerNetworkCallback() {
        while (true) {
            val register = try {
                onRegisterCallback(manager)
                true
            } catch (e: RuntimeException) {
                e.printStackTrace()
                false
            }
            val networkState = manager.currentNetworkState()
            onRegisterCallbackResult(register, networkState)
            if (register) {
                break
            } else {
                delay(1_000)
                continue
            }
        }
    }

    protected abstract fun onRegisterCallback(manager: ConnectivityManager)
    protected abstract fun onRegisterCallbackResult(register: Boolean, networkState: NetworkState?)

    init {
        MainScope().launch {
            registerNetworkCallback()
        }
    }
}

@SuppressLint("MissingPermission")
internal fun ConnectivityManager.currentNetworkState(): NetworkState? {
    val network = this.activeNetwork ?: return null
    val compatibility = this.getNetworkCapabilities(network) ?: return null
    return newNetworkState(network, compatibility)
}

private fun newNetworkState(
    network: Network,
    networkCapabilities: NetworkCapabilities
): NetworkState {
    return NetworkStateModel(
        netId = network.toString(),
        transportWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
        transportCellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
        transportEthernet = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET),
        netCapabilityInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    )
}