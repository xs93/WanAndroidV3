package com.github.xs93.wan.data.di

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wan.data.services.AccountService
import com.github.xs93.wan.data.services.CollectService
import com.github.xs93.wan.data.services.HomeService
import com.github.xs93.wan.data.services.NavigatorService
import com.github.xs93.wan.data.services.SquareService
import com.github.xs93.wan.data.services.WenDaService
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
    fun provideAccountService(): AccountService = EasyRetrofit.create(AccountService::class.java)

    @Provides
    fun provideCollectService(): CollectService = EasyRetrofit.create(CollectService::class.java)

    @Provides
    fun provideHomeService(): HomeService = EasyRetrofit.create(HomeService::class.java)

    @Provides
    fun provideSquareService(): SquareService = EasyRetrofit.create(SquareService::class.java)

    @Provides
    fun provideWenDaService(): WenDaService = EasyRetrofit.create(WenDaService::class.java)

    @Provides
    fun provideNavigatorService(): NavigatorService =
        EasyRetrofit.create(NavigatorService::class.java)
}