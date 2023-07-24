package com.github.xs93.framework.core.base.viewmodel

import android.widget.Toast
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
interface IUiEvent

@Keep
sealed class CommonUiEvent : IUiEvent {
    data class ShowToast(val charSequence: CharSequence, val duration: Int = Toast.LENGTH_SHORT) : CommonUiEvent()
    data class ShowLoadingDialog(val message: CharSequence? = null) : CommonUiEvent()
    data class UpdateLoadingDialog(val message: CharSequence) : CommonUiEvent()
    object HideLoadingDialog : CommonUiEvent()
}

@Keep
interface IUiAction

@Keep
interface IUIState
