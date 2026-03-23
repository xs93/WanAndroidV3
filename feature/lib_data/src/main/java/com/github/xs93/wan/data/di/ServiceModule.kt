package com.github.xs93.wan.data.di

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wan.data.api.AccountApi
import com.github.xs93.wan.data.api.CollectApi
import com.github.xs93.wan.data.api.HomeApi
import com.github.xs93.wan.data.api.NavigatorApi
import com.github.xs93.wan.data.api.SquareApi
import com.github.xs93.wan.data.api.WenDaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description service 接口依赖注入
 *
 */
@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {

    @Provides
    fun provideAccountApi(): AccountApi = EasyRetrofit.create(AccountApi::class.java)

    @Provides
    fun provideCollectApi(): CollectApi = EasyRetrofit.create(CollectApi::class.java)

    @Provides
    fun provideHomeApi(): HomeApi = EasyRetrofit.create(HomeApi::class.java)

    @Provides
    fun provideSquareApi(): SquareApi = EasyRetrofit.create(SquareApi::class.java)

    @Provides
    fun provideWenDaApi(): WenDaApi = EasyRetrofit.create(WenDaApi::class.java)

    @Provides
    fun provideNavigatorApi(): NavigatorApi = EasyRetrofit.create(NavigatorApi::class.java)
}