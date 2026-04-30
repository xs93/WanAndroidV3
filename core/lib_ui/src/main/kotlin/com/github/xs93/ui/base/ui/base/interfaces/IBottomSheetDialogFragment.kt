package com.github.xs93.ui.base.ui.base.interfaces

import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/19 16:29
 * @description BottomSheetDialogFragment 接口
 *
 */
interface IBottomSheetDialogFragment : IDialogFragment {

    /**
     * 获取BottomSheetBehavior
     */
    fun getSheetBehavior(): BottomSheetBehavior<*>?

    /**
     * 配置BottomSheetBehavior
     */
    fun withSheetBehavior(block: BottomSheetBehavior<*>.() -> Unit)

    /**
     * 适配内容高度
     */
    fun warpContentHeight(maxHeight: Int = -1)

    /**
     * 固定高度
     */
    fun setFixedHeight(height: Int)
}