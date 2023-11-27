package com.github.xs93.framework.base.ui.utils

/**
 * DialogFragment 配置
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/11/27 13:13
 * @email 466911254@qq.com
 */
object BaseDialogFragmentConfig {

    /**
     * 全局同意的DialogTheme，用于统一配置 BaseDialogFragment 的Theme,如需特殊定制，需要重写BaseDialogFragment的
     * [com.github.xs93.framework.base.ui.base.BaseDialogFragment.getCustomStyle]方法
     */
    var commonDialogTheme = 0

    /**
     * 全局统一的BottomSheetDialogTheme，用于统一配置 BaseBottomSheetDialogFragment的 Theme,如需特殊定制，需要重写BaseDialogFragment的
     * [com.github.xs93.framework.base.ui.base.BaseBottomSheetDialogFragment.getCustomStyle]方法
     */
    var commonBottomSheetDialogTheme = 0
}