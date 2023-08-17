package com.github.xs93.wanandroid.app.di

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.app.api.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 向外提供services 对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 10:33
 * @email 466911254@qq.com
 */

@Module
@InstallIn(SingletonComponent::class)
object ServerModel {

    @Singleton
    @Provides
    fun provideHomeService(): HomeService = EasyRetrofit.create(service = HomeService::class.java)
}