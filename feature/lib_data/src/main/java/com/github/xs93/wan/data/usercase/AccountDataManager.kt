package com.github.xs93.wan.data.usercase

import androidx.core.net.toUri
import com.github.xs93.core.AppInject
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wan.data.DataConstant
import com.github.xs93.wan.data.entity.User
import com.github.xs93.wan.data.entity.UserDetailInfo
import com.github.xs93.wan.data.model.AccountState
import com.github.xs93.wan.data.model.isLogin
import com.github.xs93.wan.data.store.AccountStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
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
class AccountDataManager @Inject constructor() {

    private val _accountStateFlow = MutableStateFlow<AccountState>(AccountState.LogOut)
    val accountStateFlow: StateFlow<AccountState> = _accountStateFlow.asStateFlow()

    private val _userDetailStateFlow = MutableStateFlow<UserDetailInfo?>(null)
    val userDetailFlow: Flow<UserDetailInfo> = _userDetailStateFlow.asStateFlow().filterNotNull()

    fun peekUserDetailInfo(): UserDetailInfo? = _userDetailStateFlow.value

    val isLogin: Boolean
        get() = _accountStateFlow.value.isLogin

    val userId: Int
        get() = peekUserDetailInfo()?.userInfo?.id ?: 0

    fun isMe(userId: Int): Boolean {
        return if (userId == 0) false else this.userId == userId
    }

    fun saveUserInfo(user: User) {
        AccountStore.userInfo = user
        _accountStateFlow.value = AccountState.LogIn(user, false)
    }


    fun saveUserDetailInfo(userDetailInfo: UserDetailInfo) {
        AccountStore.userDetailInfo = userDetailInfo
        _userDetailStateFlow.value = userDetailInfo
    }

    fun clearUserInfo() {
        AccountStore.userInfo = null
        AccountStore.userDetailInfo = null
        _accountStateFlow.value = AccountState.LogOut
        _userDetailStateFlow.value = UserDetailInfo()
    }

    init {
        AppInject.mainScope.launch(Dispatchers.IO) {
            val uri = DataConstant.BASE_URL.toUri()
            val httpUrl = HttpUrl.Builder()
                .scheme(uri.scheme!!)
                .host(uri.host!!)
                .build()
            val cookieJar = EasyRetrofit.getOkHttpClient()?.cookieJar
            val cookies = cookieJar?.loadForRequest(httpUrl)
            //必须保证有这2个cookie才能算登录成功
            val loginCookie = cookies?.firstOrNull { it.name == "loginUserName" }
            val tokenCookie = cookies?.firstOrNull { it.name == "token_pass" }
            val userInfo = AccountStore.userInfo
            if (loginCookie == null || tokenCookie == null || userInfo == null) {
                _accountStateFlow.value = AccountState.LogOut
            } else {
                _accountStateFlow.value = AccountState.LogIn(userInfo, true)
            }
            val userDetailInfo = AccountStore.userDetailInfo
            userDetailInfo?.let { _userDetailStateFlow.emit(it) }
        }
    }
}