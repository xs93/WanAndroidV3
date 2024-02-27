package com.github.xs93.wanandroid.app.model

import androidx.annotation.StringRes
import com.github.xs93.wanandroid.app.R

/**
 * home 页面tab
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/12/25 9:42
 * @email 466911254@qq.com
 */
enum class HomeTab(@StringRes val tabNameStringResId: Int) {
    Explore(R.string.home_tab_explore),
    Square(R.string.home_tab_square),
    Answer(R.string.home_tab_answer)
}