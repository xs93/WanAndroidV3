package com.github.xs93.wan.domain.usecase

import com.github.xs93.utils.AppInject
import com.github.xs93.wan.data.event.CollectEvent
import com.github.xs93.wan.data.services.CollectService
import com.github.xs93.wan.data.usercase.AccountDataManager
import com.github.xs93.wan.router.Router
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  收藏相关
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:43
 * @email 466911254@qq.com
 */
class CollectUserCase @Inject constructor(
    private val collectService: CollectService,
    private val accountDataManager: AccountDataManager,
) {
    private val _collectArticleFlow = MutableSharedFlow<CollectEvent>()
    val collectArticleEventFlow: SharedFlow<CollectEvent> = _collectArticleFlow

    fun articleCollectAction(event: CollectEvent) {
        AppInject.mainScope.launch {
            if (accountDataManager.isLogin) {
                collectService.isCollectArticle(event.collect, event.id)
                    .onSuccess {
                        _collectArticleFlow.emit(event)
                    }
            } else {
                Router.toLogin()
            }
        }
    }
}