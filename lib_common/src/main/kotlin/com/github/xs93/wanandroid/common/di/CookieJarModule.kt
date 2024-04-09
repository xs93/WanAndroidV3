package com.github.xs93.wanandroid.common.di

import android.content.Context
import com.github.xs93.network.cookie.CookieJarManager
import com.github.xs93.network.cookie.SharedPreferencesCookieStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import javax.inject.Singleton

/**
 * 提供全局的CookieJar
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:52
 * @email 466911254@qq.com
 */
@InstallIn(SingletonComponent::class)
@Module
object CookieJarModule {

    @Singleton
    @Provides
    fun provideCookieJar(@ApplicationContext context: Context): CookieJar {
        return CookieJarManager(SharedPreferencesCookieStore(context))
    }
}