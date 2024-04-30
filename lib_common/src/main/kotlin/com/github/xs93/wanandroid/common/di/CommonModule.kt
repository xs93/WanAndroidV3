package com.github.xs93.wanandroid.common.di

import com.github.xs93.wanandroid.common.web.WebViewPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/16 9:41
 * @email 466911254@qq.com
 */

@InstallIn(SingletonComponent::class)
@Module
object CommonModule {

    @Singleton
    @Provides
    fun providerWebViewPool() = WebViewPool(2)
}