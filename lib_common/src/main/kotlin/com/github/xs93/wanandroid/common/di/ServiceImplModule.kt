package com.github.xs93.wanandroid.common.di

import com.github.xs93.wanandroid.common.services.AccountService
import com.github.xs93.wanandroid.common.services.impl.AccountServiceImpl
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
}