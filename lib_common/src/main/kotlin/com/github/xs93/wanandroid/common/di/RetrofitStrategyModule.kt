package com.github.xs93.wanandroid.common.di

import com.github.xs93.wanandroid.common.network.WanRetrofitBuildStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import javax.inject.Singleton

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:56
 * @email 466911254@qq.com
 */
@InstallIn(SingletonComponent::class)
@Module
object RetrofitStrategyModule {

    @Singleton
    @Provides
    fun providerWanRetrofitStrategy(cookieJar: CookieJar) = WanRetrofitBuildStrategy(cookieJar)
}