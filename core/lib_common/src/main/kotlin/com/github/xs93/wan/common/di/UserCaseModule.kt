package com.github.xs93.wan.common.di

import com.github.xs93.wan.common.account.AccountDataManager
import com.github.xs93.wan.common.data.services.CollectService
import com.github.xs93.wan.common.data.usercase.CollectUserCase
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
object UserCaseModule {

    @Singleton
    @Provides
    fun provideAppCollectDataModel(
        collectService: CollectService,
        accountDataManager: AccountDataManager
    ) = CollectUserCase(collectService, accountDataManager)
}