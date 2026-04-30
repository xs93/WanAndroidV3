package com.github.xs93.wan.main.service

import com.github.xs93.wan.common.service.IMainService
import com.github.xs93.wan.model.event.MainDrawerOpEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/4/30 17:34
 * @description IMainService现类
 * 
 */
class MainServiceImpl @Inject constructor() : IMainService {

    private val _drawer = MutableSharedFlow<MainDrawerOpEvent>()

    override val drawerOpEventFlow: SharedFlow<MainDrawerOpEvent>
        get() = _drawer

    override fun openDrawer() {
        _drawer.tryEmit(MainDrawerOpEvent(true))
    }
}