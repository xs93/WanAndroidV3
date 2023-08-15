package com.github.xs93.wanandroid.app.ui.main

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.github.xs93.framework.adapter.ViewPager2FragmentAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.framework.ktx.isLightStatusBarsCompat
import com.github.xs93.framework.ktx.isStatusBarTranslucentCompat
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ActivityMainBinding
import com.github.xs93.wanandroid.app.ui.classify.ClassifyFragment
import com.github.xs93.wanandroid.app.ui.home.HomeFragment
import com.github.xs93.wanandroid.app.ui.mine.MineFragment
import com.github.xs93.wanandroid.app.ui.system.SystemFragment
import kotlinx.coroutines.delay

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:29
 * @email 466911254@qq.com
 */
class MainActivity : BaseVbActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var splashScreen: SplashScreen
    private var keepOnScreenCondition = true

    private lateinit var mContentAdapter: ViewPager2FragmentAdapter

    private val homeFragment = HomeFragment.newInstance()
    private val classifyFragment = ClassifyFragment.newInstance()
    private val systemFragment = SystemFragment.newInstance()
    private val mineFragment = MineFragment.newInstance()

    private val fragments by lazy {
        listOf(homeFragment, classifyFragment, systemFragment, mineFragment)
    }


    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {
        super.beforeSuperOnCreate(savedInstanceState)
        splashScreen = installSplashScreen()
    }

    override fun beforeInitView(savedInstanceState: Bundle?) {
        super.beforeInitView(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            keepOnScreenCondition
        }
        launcher {
            delay(1500L)
            keepOnScreenCondition = false
            initView(savedInstanceState)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        window.apply {
            isStatusBarTranslucentCompat = true
            isLightStatusBarsCompat = true
        }

        if (keepOnScreenCondition) {
            return
        }

        mContentAdapter = ViewPager2FragmentAdapter(supportFragmentManager, lifecycle, fragments)
        binding.vpContent.apply {
            offscreenPageLimit = fragments.size
            adapter = mContentAdapter
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavView.menu.getItem(position).isChecked = true
                }
            })
        }
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> binding.vpContent.setCurrentItem(0, false)
                R.id.menu_classify -> binding.vpContent.setCurrentItem(1, false)
                R.id.menu_system -> binding.vpContent.setCurrentItem(2, false)
                R.id.menu_mine -> binding.vpContent.setCurrentItem(3, false)
            }
            true
        }
    }
}