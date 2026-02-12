package com.github.xs93.wan.data.respotory

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wan.data.entity.Navigation
import com.github.xs93.wan.data.model.WanResponse
import com.github.xs93.wan.data.services.NavigatorService
import javax.inject.Inject

/**
 * NavigatorService 实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 16:58
 * @email 466911254@qq.com
 */
class NavigatorRepository @Inject constructor(private val navigatorService: NavigatorService) :
    BaseRepository() {

    suspend fun getNavigationList(): Result<WanResponse<List<Navigation>>> {
        return navigatorService.getNavigationList()
    }
}