package com.github.xs93.wan.domain.di

import com.github.xs93.wan.data.services.CollectService
import com.github.xs93.wan.data.usercase.AccountDataManager
import com.github.xs93.wan.domain.usecase.CollectUserCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description UserCase依赖注入
 *
 */
@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideAppCollectDataModel(
        collectService: CollectService,
        accountDataManager: AccountDataManager
    ) = CollectUserCase(collectService, accountDataManager)
}