package com.github.xs93.wanandroid.common.account

import androidx.core.net.toUri
import com.github.xs93.utils.AppInject
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.github.xs93.wanandroid.common.store.AccountStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

/**
 * 账号管理器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 11:08
 * @email 466911254@qq.com
 */
class AccountManager @Inject constructor(private val cookieJar: CookieJar) {

    private val _accountStateFlow: MutableStateFlow<AccountState> = MutableStateFlow(AccountState.LogOut)
    private val _userDetailStateFlow: MutableStateFlow<UserDetailInfo> = MutableStateFlow(UserDetailInfo())

    init {
        AppInject.mainScope.launch {
            val uri = AppConstant.BaseUrl.toUri()
            val httpUrl = HttpUrl.Builder()
                .scheme(uri.scheme!!)
                .host(uri.host!!)
                .build()
            val cookies = cookieJar.loadForRequest(httpUrl)
            val userInfo = AccountStore.userInfo
            if (cookies.isEmpty() || userInfo == null) {
                _accountStateFlow.emit(AccountState.LogOut)
            } else {
                _accountStateFlow.emit(AccountState.LogIn(true, userInfo))
            }

            val userDetailInfo = AccountStore.userDetailInfo
            userDetailInfo?.let { _userDetailStateFlow.emit(it) }
        }
    }

    val accountStateFlow: StateFlow<AccountState> = _accountStateFlow

    val userDetailFlow: StateFlow<UserDetailInfo> = _userDetailStateFlow

    fun peekUserDetailInfo(): UserDetailInfo = userDetailFlow.value

    val isLogin: Boolean
        get() = _accountStateFlow.value.isLogin

    val userId: Int
        get() = peekUserDetailInfo().userInfo.id

    fun isMe(userId: Int): Boolean {
        return if (userId == 0) false else this.userId == userId
    }

    fun logIn(user: User) {
        AppInject.mainScope.launch {
            AccountStore.userInfo = user
            _accountStateFlow.emit(AccountState.LogIn(false, user))
        }
    }

    fun logout() {
        AppInject.mainScope.launch {
            AccountStore.userInfo = null
            AccountStore.userDetailInfo = null
            _userDetailStateFlow.emit(UserDetailInfo())
            _accountStateFlow.emit(AccountState.LogOut)
        }
    }

    fun cacheUserDetailInfo(userDetailInfo: UserDetailInfo) {
        AppInject.mainScope.launch {
            AccountStore.userDetailInfo = userDetailInfo
            _userDetailStateFlow.emit(userDetailInfo)
        }
    }
}