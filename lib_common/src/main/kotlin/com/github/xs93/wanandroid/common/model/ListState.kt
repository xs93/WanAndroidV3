package com.github.xs93.wanandroid.common.model

/**
 * 列表状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/27 10:39
 * @email 466911254@qq.com
 */


data class ListState<out T>(
    val listUiState: ListUiState = ListUiState.IDLE,
    val data: List<T> = listOf(),
    val curPage: Int = -1,
    val updateDataMethod: ListUpdateDataMethod = ListUpdateDataMethod.Reset,
    val noMoreData: Boolean = false
)

sealed class ListUpdateDataMethod {
    data object Reset : ListUpdateDataMethod()

    data class Update(val scrollToTop: Boolean = false) : ListUpdateDataMethod()
}

sealed class ListUiState {
    /**
     * 当前Ui处于默认状态
     */
    data object IDLE : ListUiState()

    /**
     * 当前列表处于刷新数据状态
     * @constructor
     */
    data object Refreshing : ListUiState()

    /**
     * 当前列表处于更新Ui状态
     * @constructor
     */
    data object LoadMore : ListUiState()

    /**
     * 当前列表处于刷新数据完成状态
     * @param success Boolean 数据请求是否成功
     * @constructor
     */
    data class RefreshFinished(val success: Boolean, val error: Throwable? = null) : ListUiState()

    /**
     * 当前列表处于数据加载更多完成状态
     * @param success Boolean 数据请求是否成功
     * @constructor
     */
    data class LoadMoreFinished(val success: Boolean, val error: Throwable? = null) : ListUiState()
}