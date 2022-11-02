package com.github.xs93.wanandroid.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.github.xs93.core.base.ui.vbvm.BaseVbVmActivity
import com.github.xs93.core.bus.FlowBus
import com.github.xs93.core.ktx.isStatusBarTranslucentCompat
import com.github.xs93.core.ktx.repeatOnLifecycle
import com.github.xs93.core.utils.toast.ToastUtils
import com.github.xs93.wanandroid.common.bus.FlowBusKey
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.common.viewmodel.MainShareViewModel
import com.github.xs93.wanandroid.main.databinding.ActivityMainBinding
import com.github.xs93.wanandroid.main.databinding.MainContentBinding
import com.github.xs93.wanandroid.main.databinding.MainNavHeaderBinding

/**
 * 首页界面
 *
 *
 * @author xushuai
 * @date   2022/9/3-11:19
 * @email  466911254@qq.com
 */
class MainActivity : BaseVbVmActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    private lateinit var mainShareViewModel: MainShareViewModel
    private lateinit var mContentBinding: MainContentBinding
    private lateinit var mNavHeaderBinding: MainNavHeaderBinding

    private lateinit var mContentAdapter: ContentAdapter

    override fun initView(savedInstanceState: Bundle?) {
        window.apply {
            isStatusBarTranslucentCompat = true
        }

        mainShareViewModel = ViewModelProvider(this)[MainShareViewModel::class.java]

        val clickListener = Listener()



        mNavHeaderBinding = MainNavHeaderBinding.bind(binding.navView.getHeaderView(0)).apply {
            listener = clickListener
        }

        mContentBinding = binding.layoutContent
        val fragments = arrayListOf<Fragment>().apply {
            add(ARouter.getInstance().build(RouterPath.Home.HomeFragment).navigation() as Fragment)
        }
        mContentAdapter = ContentAdapter(fragments, supportFragmentManager, lifecycle)

        mContentBinding.vpContent.apply {
            isUserInputEnabled = false
            offscreenPageLimit = fragments.size
            adapter = mContentAdapter
        }

        binding.apply {
            surface = this@MainActivity.surface
            listener = clickListener
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        FlowBus.subscribe<Boolean>(FlowBusKey.LOGIN_STATUS, this) {
            viewModel.input(MainIntent.LoginEventAction(it))
        }

        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.stateFlow.collect {
                mNavHeaderBinding.state = it
                binding.state = it
            }
        }

        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.eventFlow.collect {
                when (it) {
                    is MainEvent.ToastEvent -> {
                        ToastUtils.show(it.msg)
                    }
                    is MainEvent.OpenDrawerEvent -> {
                        binding.drawerLayout.openDrawer(GravityCompat.START)
                    }
                    is MainEvent.CloseDrawerEvent -> {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            viewModel.input(MainIntent.CloseDrawerAction)
        } else {
            super.onBackPressed()
        }
    }

    inner class Listener {
        fun clickNavLogin() {
            viewModel.input(MainIntent.ClickLoginAction)
        }

        fun clickToolbarNav() {
            viewModel.input(MainIntent.OpenDrawerAction)
        }

        fun clickLogout() {

        }
    }
}