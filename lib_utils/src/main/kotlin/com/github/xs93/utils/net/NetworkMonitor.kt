package com.github.xs93.utils.net

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 网络状态变化监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/26 10:23
 * @email 466911254@qq.com
 */

object NetworkMonitor {

    private var mApplication: Application? = null

    private val mListener = CopyOnWriteArrayList<NetworkStateListener>()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun init(application: Application) {
        mApplication = application
        registerListener()
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun registerListener() {
        val application = mApplication ?: throw NullPointerException("please call Init() method")
        val cm = ContextCompat.getSystemService(application, ConnectivityManager::class.java)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                if (cm == null) {
                    return
                }
                cm.registerDefaultNetworkCallback(NetworkCallback(application))
            }

            else -> {
                if (cm == null) {
                    return
                }
                val request: NetworkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
                cm.registerNetworkCallback(request, NetworkCallback(application))
            }
        }
    }

    fun observer(lifecycle: Lifecycle, listener: NetworkStateListener) {
        mListener.add(listener)
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    lifecycle.removeObserver(this)
                    mListener.remove(listener)
                }
            }
        })
    }

    @Synchronized
    private fun notifyListener(networkType: NetworkType) {
        val isConnected =
            networkType == NetworkType.NETWORK_WIFI || networkType == NetworkType.NETWORK_MOBILE
        mListener.forEach {
            it.invoke(isConnected, networkType)
        }
    }


    @SuppressLint("MissingPermission")
    private class NetworkCallback(private val context: Context) :
        ConnectivityManager.NetworkCallback() {

        private var mLastNetworkType: NetworkType = NetworkType.NETWORK_NONE


        override fun onLost(network: Network) {
            super.onLost(network)
            val networkType = context.getNetworkType()
            notifyListener(networkType)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val networkType = context.getNetworkType(network)
            if (mLastNetworkType != networkType) {
                notifyListener(networkType)
                mLastNetworkType = networkType
            }
        }
    }
}


typealias NetworkStateListener = (isConnected: Boolean, networkType: NetworkType) -> Unit


