package com.github.xs93.wanandroid.common.di

import com.github.xs93.wanandroid.common.services.AccountService
import com.github.xs93.wanandroid.common.services.CollectService
import com.github.xs93.wanandroid.common.services.HomeService
import com.github.xs93.wanandroid.common.services.NavigatorService
import com.github.xs93.wanandroid.common.services.SquareService
import com.github.xs93.wanandroid.common.services.WenDaService
import com.github.xs93.wanandroid.common.services.impl.AccountServiceImpl
import com.github.xs93.wanandroid.common.services.impl.CollectServiceImpl
import com.github.xs93.wanandroid.common.services.impl.HomeServiceImpl
import com.github.xs93.wanandroid.common.services.impl.NavigatorServiceImpl
import com.github.xs93.wanandroid.common.services.impl.SquareServiceImpl
import com.github.xs93.wanandroid.common.services.impl.WenDaServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 16:31
 * @email 466911254@qq.com
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class ServiceImplModule {

    @Binds
    @Singleton
    abstract fun getAccountServiceImpl(impl: AccountServiceImpl): AccountService

    @Binds
    @Singleton
    abstract fun getCollectServiceImpl(impl: CollectServiceImpl): CollectService

    @Binds
    @Singleton
    abstract fun getHomeServiceImpl(impl: HomeServiceImpl): HomeService

    @Binds
    @Singleton
    abstract fun getSquareServiceImpl(impl: SquareServiceImpl): SquareService

    @Binds
    @Singleton
    abstract fun getWenDaServiceImpl(impl: WenDaServiceImpl): WenDaService

    @Binds
    @Singleton
    abstract fun getNavigatorServiceImpl(impl: NavigatorServiceImpl): NavigatorService
}