package com.github.xs93.wanandroid.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:51
 * @email 466911254@qq.com
 */
class ContentAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}