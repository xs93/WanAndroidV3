package com.github.xs93.wan.domain.usecase

import com.github.xs93.wan.data.respotory.CollectRepository
import com.github.xs93.wan.data.usercase.AccountDataManager
import com.github.xs93.wan.model.event.CollectEvent
import com.github.xs93.wan.router.RouterHelper
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _collectEventFlow = MutableSharedFlow<CollectEvent>(
        replay = 0,
        extraBufferCapacity = 4,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val collectEventFlow: SharedFlow<CollectEvent> = _collectEventFlow.asSharedFlow()

    suspend operator fun invoke(event: CollectEvent) {
        if (accountDataManager.isLogin) {
            collectRepository.collectOrNotArticle(event.collect, event.id)
                .onSuccess {
                    if (it.isSuccess()) {
                        _collectEventFlow.emit(event)
                    }
                }
        } else {
            RouterHelper.toLogin()
        }
    }
}