package com.chatbot.bkfire.club.compose.app.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chatbot.bkfire.club.compose.app.R

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/7 17:30
 * @email 466911254@qq.com
 */
enum class MainTab(@StringRes val tabNameStringResId: Int, @DrawableRes val tabIconResId: Int) {

    Home(R.string.main_bottom_menu_home, R.drawable.main_ic_nav_menu_home),
    Classify(R.string.main_bottom_menu_classify, R.drawable.main_ic_nav_menu_tree),
    System(R.string.main_bottom_menu_system, R.drawable.main_ic_nav_menu_navigation),
    Mine(R.string.main_bottom_menu_mine, R.drawable.main_ic_nav_menu_mine)
}