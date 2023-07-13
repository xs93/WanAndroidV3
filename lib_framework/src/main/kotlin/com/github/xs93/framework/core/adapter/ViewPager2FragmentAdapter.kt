package com.github.xs93.framework.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * ViewPager2 Fragment 适配器，
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:10
 * @email 466911254@qq.com
 */
class ViewPager2FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}