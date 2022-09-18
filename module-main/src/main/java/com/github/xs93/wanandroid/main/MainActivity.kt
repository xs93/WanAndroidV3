package com.github.xs93.wanandroid.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.github.xs93.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.core.bus.FlowBus
import com.github.xs93.core.ktx.isStatusBarTranslucentCompat
import com.github.xs93.wanandroid.common.bus.FlowBusKey
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.common.viewmodel.MainShareViewModel
import com.github.xs93.wanandroid.main.databinding.ActivityMainBinding
import com.github.xs93.wanandroid.main.databinding.MainContentBinding
import com.github.xs93.wanandroid.main.databinding.MainNavHeaderBinding
import kotlinx.coroutines.launch

/**
 * 首页界面
 *
 *
 * @author xushuai
 * @date   2022/9/3-11:19
 * @email  466911254@qq.com
 */
class MainActivity : BaseVbActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var mainShareViewModel: MainShareViewModel
    private lateinit var mContentBinding: MainContentBinding
    private lateinit var mNavHeaderBinding: MainNavHeaderBinding


    override fun initView(savedInstanceState: Bundle?) {
        window.apply {
            isStatusBarTranslucentCompat = true
        }
        mBinding.surface = surface

        mContentBinding = mBinding.layoutContent
        mNavHeaderBinding = MainNavHeaderBinding.bind(mBinding.navView.getHeaderView(0))

        mNavHeaderBinding.activity = this

        mainShareViewModel = ViewModelProvider(this)[MainShareViewModel::class.java]

        mContentBinding.toolbar.setNavigationOnClickListener {
            mainShareViewModel.openDrawerLayout()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainShareViewModel.drawerLayoutState.collect {
                    if (it) {
                        mBinding.drawerLayout.openDrawer(GravityCompat.START)
                    } else {
                        mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
            }
        }
    }


    override fun initData(savedInstanceState: Bundle?) {
        FlowBus.subscribe<Boolean>(FlowBusKey.LOGIN_STATUS, this) {
            mNavHeaderBinding.login = it
        }
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun clickNavLogin() {
        ARouter.getInstance().build(RouterPath.Login.LoginActivity).navigation()
    }
}