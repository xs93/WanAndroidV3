package com.github.xs93.wanandroid.app.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import com.github.xs93.framework.adapter.SimpleViewPagerAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.addOnBackPressedCallback
import com.github.xs93.framework.ktx.isLightStatusBarsCompat
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.framework.ktx.observerEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.framework.ktx.setTouchSlopMultiple
import com.github.xs93.utils.ktx.gone
import com.github.xs93.utils.ktx.isNightMode
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.startActivitySafe
import com.github.xs93.utils.ktx.string
import com.github.xs93.utils.ktx.visible
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.MainActivityBinding
import com.github.xs93.wanandroid.app.ui.classify.ClassifyFragment
import com.github.xs93.wanandroid.app.ui.home.HomeFragment
import com.github.xs93.wanandroid.app.ui.login.LoginActivity
import com.github.xs93.wanandroid.app.ui.mine.MineFragment
import com.github.xs93.wanandroid.app.ui.system.SystemFragment
import com.github.xs93.wanandroid.common.account.AccountState
import com.github.xs93.wanandroid.common.data.AccountDataModule
import com.github.xs93.wanandroid.common.data.CollectDataModel
import com.github.xs93.wanandroid.common.store.AppCommonStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 主页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:29
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<MainActivityBinding>(R.layout.main_activity, MainActivityBinding::bind) {

    private lateinit var splashScreen: SplashScreen
    private var keepOnScreenCondition = true

    private lateinit var mContentAdapter: SimpleViewPagerAdapter

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var collectDataModel: CollectDataModel

    @Inject
    lateinit var accountDataModule: AccountDataModule

    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {
        super.beforeSuperOnCreate(savedInstanceState)
        splashScreen = installSplashScreen()
    }

    override fun beforeSetContentView(savedInstanceState: Bundle?) {
        super.beforeSetContentView(savedInstanceState)
        if (savedInstanceState != null) {
            keepOnScreenCondition = false
        } else {
            splashScreen.setKeepOnScreenCondition {
                keepOnScreenCondition
            }
            launcher {
                delay(1500L)
                keepOnScreenCondition = false
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mContentAdapter = SimpleViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            add { HomeFragment.newInstance() }
            add { ClassifyFragment.newInstance() }
            add { SystemFragment.newInstance() }
            add { MineFragment.newInstance() }
        }

        binding.apply {
            with(drawerRoot) {
                addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                    override fun onDrawerOpened(drawerView: View) {
                        if (drawerView.id == R.id.main_drawer_layout) {
                            window.isLightStatusBarsCompat = false
                        }
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        if (drawerView.id == R.id.main_drawer_layout) {
                            window.isLightStatusBarsCompat = !isNightMode
                        }
                    }
                })
            }
        }

        binding.mainContentLayout.apply {
            with(vpContent) {
                offscreenPageLimit = mContentAdapter.itemCount
                adapter = mContentAdapter
                setTouchSlopMultiple(2f)
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


        binding.mainDrawerLayout.apply {
            with(btnLogin) {
                setSingleClickListener {
                    startActivitySafe<LoginActivity>()
                }
            }

            with(btnLogout) {
                setOnClickListener {
                    mainViewModel.mainActions.send(MainAction.LogoutAction)
                }
            }

            with(btnThemeMode) {
                setOnClickListener {
                    AppCommonStore.userCustomNightMode = true
                    switchThemeMode.toggle()
                }
            }

            with(switchThemeMode) {
                isChecked = if (AppCommonStore.userCustomNightMode) {
                    AppCommonStore.isNightMode
                } else {
                    isNightMode
                }
                setOnCheckedChangeListener { _, isChecked ->
                    AppCommonStore.isNightMode = isChecked
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    recreate()
                }
            }
        }

        addOnBackPressedCallback(true) {
            moveTaskToBack(false)
        }

    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        mainViewModel.registerCommonEvent(this)

        observerEvent(mainViewModel.mainEventFlow) {
            when (it) {
                MainEvent.OpenDrawerEvent -> {
                    binding.drawerRoot.openDrawer(GravityCompat.START)
                }
            }
        }

        observerState(accountDataModule.accountState) {
            when (it) {
                AccountState.LogOut -> {
                    with(binding.mainDrawerLayout) {
                        btnLogin.visible()
                        btnLogout.gone()
                        groupUserInfo.gone()
                        imgAvatar.load(R.drawable.img_avatar_no_login)
                    }
                }

                is AccountState.LogIn -> {
                    with(binding.mainDrawerLayout) {
                        btnLogin.gone()
                        btnLogout.visible()
                        groupUserInfo.visible()
                        imgAvatar.load(R.drawable.img_avatar_login)
                    }
                }
            }
        }

        observerState(accountDataModule.userDetailFlow) {
            with(binding.mainDrawerLayout) {
                txtNickname.text = it.userInfo.nickname
                txtUserId.text = string(R.string.main_drawer_user_id, it.userInfo.id)
                txtCoinInfo.text = string(
                    R.string.main_drawer_user_coin_info,
                    it.coinInfo.coinCount,
                    it.coinInfo.level,
                    it.coinInfo.rank
                )
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (binding.drawerRoot.isDrawerOpen(GravityCompat.START)) {
            window.isLightStatusBarsCompat = false
        }
    }
}