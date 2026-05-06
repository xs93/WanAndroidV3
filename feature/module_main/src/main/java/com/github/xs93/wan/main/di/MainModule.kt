package com.github.xs93.wan.main.di

import com.github.xs93.wan.common.service.IMainService
import com.github.xs93.wan.main.service.MainServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/4/30 17:42
 * @description Main 模块依赖注入
 * 
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class MainModule {

    /**
     * 绑定 MainService
     */
    @Binds
    @ActivityRetainedScoped
    abstract fun bindMainService(impl: MainServiceImpl): IMainService
}