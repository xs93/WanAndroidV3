package com.github.xs93.wanandroid.common.model

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 列表当前数据状态
 *
 */
data class ListState<out T>(
    val listUiState: ListUiState = ListUiState.IDLE,
    val data: List<T> = listOf(),
    val curPage: Int = -1
)

sealed interface ListUiState {

    /**
     * 当前Ui处于默认状态
     */
    data object IDLE : ListUiState

    /**
     * 当前列表处于请求数据状态
     * @param refreshing 是否是下拉刷新
     */
    data class RequestStart(val refreshing: Boolean) : ListUiState

    /**
     * 当前列表处于请求数据完成状态
     * @param refreshing 是否是下拉刷新
     * @param noMoreData 是否没有更多数据
     */
    data class RequestFinish(val refreshing: Boolean, val noMoreData: Boolean) : ListUiState

    /**
     * 当前列表处于请求数据失败状态
     * @param refreshing 是否是下拉刷新
     * @param error 请求数据失败的异常
     */
    data class RequestFinishFailed(val refreshing: Boolean, val error: Throwable) : ListUiState
}