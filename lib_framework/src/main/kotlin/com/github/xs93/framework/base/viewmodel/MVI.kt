package com.github.xs93.framework.base.viewmodel

import androidx.annotation.Keep

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/24 10:03
 * @email 466911254@qq.com
 */

@Keep
interface IUiState

@Keep
interface IUiAction

@Keep
interface IUiEvent

@Keep
sealed class CommonUiEvent : IUiEvent {
    data object ShowLoadingDialog : CommonUiEvent()

    data object HideLoadingDialog : CommonUiEvent()
}




