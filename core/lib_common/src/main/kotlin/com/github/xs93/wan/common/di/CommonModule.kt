package com.github.xs93.wan.common.di

import com.github.xs93.wan.common.account.AccountDataManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 一些通用的依赖注入
 *
 */
@InstallIn(SingletonComponent::class)
@Module
object CommonModule {

    @Singleton
    @Provides
    fun provideAccountManager() = AccountDataManager()
}