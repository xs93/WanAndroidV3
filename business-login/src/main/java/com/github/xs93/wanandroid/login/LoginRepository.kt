package com.github.xs93.wanandroid.login

import com.github.xs93.core.base.repository.BaseRepository
import com.github.xs93.retrofit.EasyRetrofit
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.model.AccountInfo
import com.github.xs93.wanandroid.common.model.UserInfo
import com.github.xs93.wanandroid.common.store.UserStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 *
 * 登录数据仓库
 *
 * @author xushuai
 * @date   2022/9/7-15:55
 * @email  466911254@qq.com
 */
class LoginRepository : BaseRepository() {

    private val loginApi: LoginApi by lazy {
        EasyRetrofit.create(service = LoginApi::class.java)
    }

    fun login(username: String, password: String): Flow<RequestState<AccountInfo>> {
        return flow<RequestState<AccountInfo>> {
            val result = loginApi.login(username, password).coverData()
            if (result != null) {
                val userInfo = loginApi.getUserInfo().coverData()
                UserStore.apply {
                    login = true
                    this.userInfo = userInfo
                }
            }
            emit(RequestState.Success(result))
        }
            .onStart {
                emit(RequestState.Loading)
            }
            .catch { e ->
                emit(RequestState.Error(Exception(e.message)))
            }
    }

    fun register(username: String, password: String, rePassword: String): Flow<RequestState<AccountInfo>> {
        return flow<RequestState<AccountInfo>> {
            val result = loginApi.register(username, password, rePassword).coverData()
            emit(RequestState.Success(result))
        }
            .onStart {
                emit(RequestState.Loading)
            }
            .catch { e ->
                emit(RequestState.Error(Exception(e.message)))
            }
    }

    fun logout(): Flow<RequestState<Any>> {
        return flow<RequestState<Any>> {
            emit(RequestState.Success(null))
        }
            .onStart {
                emit(RequestState.Loading)
            }
            .catch { e ->
                emit(RequestState.Error(Exception(e.message)))
            }
    }

    fun getUserInfo(): Flow<RequestState<UserInfo>> {
        return flow<RequestState<UserInfo>> {
            val result = loginApi.getUserInfo().coverData()
            emit(RequestState.Success(result))
        }.onStart {
            emit(RequestState.Loading)
        }.catch { e ->
            emit(RequestState.Error(Exception(e.message)))
        }
    }
}