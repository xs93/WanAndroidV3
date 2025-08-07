package com.github.xs93.wanandroid.common.di

import com.github.xs93.wanandroid.common.account.AccountManager
import com.github.xs93.wanandroid.common.data.AccountDataModule
import com.github.xs93.wanandroid.common.data.CollectDataModel
import com.github.xs93.wanandroid.common.services.AccountService
import com.github.xs93.wanandroid.common.services.CollectService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import javax.inject.Singleton

/**
 * DataModel 对象Hilt 提供模块
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:44
 * @email 466911254@qq.com
 */
@InstallIn(SingletonComponent::class)
@Module
object DataModelModule {

    @Singleton
    @Provides
    fun providesAccountManager(cookieJar: CookieJar) = AccountManager(cookieJar)

    @Singleton
    @Provides
    fun providesAccountDataModel(accountService: AccountService, accountManager: AccountManager) =
        AccountDataModule(accountService, accountManager)

    @Singleton
    @Provides
    fun providesAppCollectDataModel(
        collectService: CollectService,
        accountDataModule: AccountDataModule
    ) =
        CollectDataModel(collectService, accountDataModule)
}