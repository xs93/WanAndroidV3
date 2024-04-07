package com.github.xs93.wanandroid.common.account

import androidx.core.net.toUri
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.utils.AppInject
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.github.xs93.wanandroid.common.store.AccountStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

/**
 * 账号管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 11:08
 * @email 466911254@qq.com
 */
object AccountManager {

    private val _userStateFlow: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _userDetailStateFlow: MutableStateFlow<UserDetailInfo?> = MutableStateFlow(null)

    init {
        AppInject.mainScope.launch {
            val cookieJar = EasyRetrofit.getRetrofitClient(AppConstant.BaseUrl).getOkHttpClient().cookieJar
            val uri = AppConstant.BaseUrl.toUri()
            val httpUrl = HttpUrl.Builder()
                .scheme(uri.scheme!!)
                .host(uri.host!!)
                .build()
            val cookies = cookieJar.loadForRequest(httpUrl)
            if (cookies.isEmpty()) {
                _userStateFlow.emit(null)
            } else {
                val userInfo = AccountStore.userInfo
                _userStateFlow.emit(userInfo)
            }

            val userDetailInfo = AccountStore.userDetailInfo
            _userDetailStateFlow.emit(userDetailInfo)
        }
    }

    val userFlow: StateFlow<User?> = _userStateFlow
    val userDetailFlow: StateFlow<UserDetailInfo?> = _userDetailStateFlow


    fun peekUserInfo(): User? = userFlow.value

    fun peekUserDetailInfo(): UserDetailInfo? = userDetailFlow.value

    val isLogin: Boolean
        get() = userFlow.value == null

    val userId: Int
        get() = userFlow.value?.id ?: 0

    fun isMe(userId: Int): Boolean {
        return if (userId == 0) false else this.userId == userId
    }

    fun logIn(user: User) {
        AppInject.mainScope.launch {
            AccountStore.userInfo = user
            _userStateFlow.emit(user)
        }
    }

    fun logout() {
        AppInject.mainScope.launch {
            AccountStore.userInfo = null
            AccountStore.userDetailInfo = null
            _userStateFlow.emit(null)
        }
    }

    fun cacheUserDetailInfo(userDetailInfo: UserDetailInfo) {
        AppInject.mainScope.launch {
            AccountStore.userDetailInfo = userDetailInfo
            _userDetailStateFlow.emit(userDetailInfo)
        }
    }
}