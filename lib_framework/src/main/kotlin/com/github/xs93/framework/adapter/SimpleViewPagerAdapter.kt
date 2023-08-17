package com.github.xs93.framework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 通用ViewPager2+Fragment的适配器,不再将fragment对象列表传入Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:10
 * @email 466911254@qq.com
 */
class SimpleViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mFragments = mutableListOf<HandleFragment>()

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position].invoke()
    }

    fun add(fragment: HandleFragment): SimpleViewPagerAdapter {
        mFragments.add(fragment)
        return this
    }

    fun add(fragments: List<HandleFragment>): SimpleViewPagerAdapter {
        mFragments.addAll(fragments)
        return this
    }
}

typealias HandleFragment = () -> Fragment