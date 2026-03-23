package com.github.xs93.wan.domain.usecase

import com.github.xs93.core.AppInject
import com.github.xs93.wan.bus.BusHelper
import com.github.xs93.wan.bus.event.CollectEvent
import com.github.xs93.wan.data.respotory.CollectRepository
import com.github.xs93.wan.data.usercase.AccountDataManager
import com.github.xs93.wan.router.RouterHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  收藏或者取消收藏的UseCase
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:43
 * @email 466911254@qq.com
 */
class CollectOrNotArticleUseCase @Inject constructor(
    private val collectRepository: CollectRepository,
    private val accountDataManager: AccountDataManager,
) {

    operator fun invoke(event: CollectEvent) {
        AppInject.mainScope.launch {
            if (accountDataManager.isLogin) {
                collectRepository.collectOrNotArticle(event.collect, event.id)
                    .onSuccess {
                        if (it.isSuccess()) {
                            BusHelper.collectEventBus.post(event)
                        }
                    }
            } else {
                RouterHelper.toLogin()
            }
        }
    }
}