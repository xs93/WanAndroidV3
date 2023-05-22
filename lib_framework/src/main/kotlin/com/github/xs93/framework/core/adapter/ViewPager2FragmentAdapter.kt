package com.github.xs93.framework.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * ViewPager2 Fragment 适配器，
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:10
 * @email 466911254@qq.com
 */
class ViewPager2FragmentAdapter(activity: FragmentActivity, var fragments: List<Fragment>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setData(fragments: List<Fragment>) {
        this.fragments = fragments
    }
}