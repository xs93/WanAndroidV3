package com.github.xs93.wanandroid.common.data

import com.github.xs93.utils.AppInject
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.services.CollectService
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
class CollectDataModel @Inject constructor(private val collectService: CollectService) {

    private val _collectArticleFlow = MutableSharedFlow<CollectEvent>()
    val collectArticleEventFlow: SharedFlow<CollectEvent> = _collectArticleFlow

    suspend fun articleCollectAction(event: CollectEvent) {
        AppInject.mainScope.launch {
            collectService.isCollectArticle(event.collect, event.id).getOrNull() ?: return@launch
            _collectArticleFlow.emit(event)
        }
    }
}