package com.github.xs93.framework.base.viewmodel

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
interface IUIState

@Keep
interface IUiAction

@Keep
interface IUiEvent

@Keep
sealed class CommonUiEvent : IUiEvent {
    data class ShowToast(val charSequence: CharSequence, val duration: Int = Toast.LENGTH_SHORT) :
        CommonUiEvent()

    object ShowLoadingDialog : CommonUiEvent()
    object HideLoadingDialog : CommonUiEvent()
}




