package com.github.xs93.framework.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/9 13:27
 * @description 权限申请工具类
 *
 */
object PermissionManager {

    private val interceptors = mutableListOf<PermissionInterceptor>()

    fun addInterceptor(interceptor: PermissionInterceptor) {
        interceptors.add(interceptor)
    }

    fun requestPermissions(
        activity: FragmentActivity,
        permissions: List<String>,
        onResult: (granted: List<String>, denied: List<String>) -> Unit
    ) {

        val filteredPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (filteredPermissions.isEmpty()) {
            onResult(permissions.toList(), emptyList())
            return
        }

        val interceptor = interceptors.firstOrNull {
            it.shouldIntercept(activity, filteredPermissions)
        }

        if (interceptor != null) {
            interceptor.intercept(activity, filteredPermissions) {
                realRequest(activity, filteredPermissions, onResult)
            }
        } else {
            realRequest(activity, filteredPermissions, onResult)
        }
    }


    private fun realRequest(
        activity: FragmentActivity,
        permissions: List<String>,
        onResult: (granted: List<String>, denied: List<String>) -> Unit
    ) {
        val launcher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                val granted = result.filter { it.value }.keys.toList()
                val denied = result.filter { !it.value }.keys.toList()
                onResult(granted, denied)
            }
        launcher.launch(permissions.toTypedArray())
    }
}