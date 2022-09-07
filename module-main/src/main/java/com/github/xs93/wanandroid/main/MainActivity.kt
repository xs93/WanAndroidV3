package com.github.xs93.wanandroid.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.xs93.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.core.ktx.isStatusBarTranslucentCompat
import com.github.xs93.wanandroid.common.viewmodel.MainShareViewModel
import com.github.xs93.wanandroid.main.databinding.ActivityMainBinding
import com.github.xs93.wanandroid.main.databinding.MainContentBinding
import kotlinx.coroutines.launch

/**
 * 首页界面
 *
 *
 * @author xushuai
 * @date   2022/9/3-11:19
 * @email  466911254@qq.com
 */
class MainActivity : BaseVbActivity<ActivityMainBinding>() {

    private lateinit var mainShareViewModel: MainShareViewModel
    private lateinit var mContentBinding: MainContentBinding

    override fun initView(savedInstanceState: Bundle?) {
        mContentBinding = mBinding.layoutContent
        mainShareViewModel = ViewModelProvider(this)[MainShareViewModel::class.java]

        window.apply {
            isStatusBarTranslucentCompat = true
        }
        mBinding.surface = surface


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
}