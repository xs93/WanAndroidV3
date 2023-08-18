package com.github.xs93.wanandroid.app.ui.main

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.github.xs93.framework.adapter.SimpleViewPagerAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ktx.addOnBackPressedCallback
import com.github.xs93.framework.ktx.isLightStatusBarsCompat
import com.github.xs93.framework.ktx.isStatusBarTranslucentCompat
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.utils.ktx.isNightMode
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.MainActivityBinding
import com.github.xs93.wanandroid.app.ui.classify.ClassifyFragment
import com.github.xs93.wanandroid.app.ui.home.HomeFragment
import com.github.xs93.wanandroid.app.ui.mine.MineFragment
import com.github.xs93.wanandroid.app.ui.system.SystemFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * 主页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:29
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<MainActivityBinding>(
    R.layout.main_activity,
    MainActivityBinding::bind
) {

    private lateinit var splashScreen: SplashScreen
    private var keepOnScreenCondition = true

    private lateinit var mContentAdapter: SimpleViewPagerAdapter

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
            isLightStatusBarsCompat = !isNightMode
        }

        if (keepOnScreenCondition) {
            return
        }

        mContentAdapter = SimpleViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            add { HomeFragment.newInstance() }
            add { ClassifyFragment.newInstance() }
            add { SystemFragment.newInstance() }
            add { MineFragment.newInstance() }
        }

        binding.mainContentLayout.apply {
            with(vpContent) {
                offscreenPageLimit = mContentAdapter.itemCount
                adapter = mContentAdapter
                registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bottomNavView.menu.getItem(position).isChecked = true
                    }
                })
            }

            with(bottomNavView) {
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.menu_home -> vpContent.setCurrentItem(0, false)
                        R.id.menu_classify -> vpContent.setCurrentItem(1, false)
                        R.id.menu_system -> vpContent.setCurrentItem(2, false)
                        R.id.menu_mine -> vpContent.setCurrentItem(3, false)
                    }
                    true
                }
            }
        }

        addOnBackPressedCallback(true) {

        }
    }
}