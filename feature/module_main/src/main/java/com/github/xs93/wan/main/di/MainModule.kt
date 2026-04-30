package com.github.xs93.wan.main.di

import com.github.xs93.wan.common.service.IMainService
import com.github.xs93.wan.main.service.MainServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/4/30 17:42
 * @description
 * 
 */
@InstallIn(SingletonComponent::class)
@Module
class MainModule {
    @Provides
    @Singleton
    fun provideMainService(): IMainService = MainServiceImpl()
}