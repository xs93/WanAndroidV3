package com.github.xs93.wanandroid.login

import com.github.xs93.core.base.repository.BaseRepository
import com.github.xs93.retrofit.EasyRetrofit
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.model.AccountModel
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

    fun login(username: String, password: String): Flow<RequestState<AccountModel>> {
        return flow<RequestState<AccountModel>> {
            val result = loginApi.login(username, password).coverData()
            emit(RequestState.Success(result))
        }
            .onStart {
                emit(RequestState.Loading)
            }
            .catch { e ->
                emit(RequestState.Error(Exception(e)))
            }
    }

    fun register(username: String, password: String, rePassword: String): Flow<RequestState<AccountModel>> {
        return flow<RequestState<AccountModel>> {
            val result = loginApi.register(username, password, rePassword).coverData()
            emit(RequestState.Success(result))
        }
            .onStart {
                emit(RequestState.Loading)
            }
            .catch { e ->
                emit(RequestState.Error(Exception(e)))
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
                emit(RequestState.Error(Exception(e)))
            }
    }

}