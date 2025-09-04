package com.github.xs93.wan.main.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import coil3.load
import com.github.xs93.framework.adapter.SimpleViewPagerAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.addOnBackPressedCallback
import com.github.xs93.framework.ktx.observerEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.framework.ktx.setTouchSlopMultiple
import com.github.xs93.utils.ktx.gone
import com.github.xs93.utils.ktx.isNightMode
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.string
import com.github.xs93.utils.ktx.visible
import com.github.xs93.wan.common.account.AccountDataManager
import com.github.xs93.wan.common.account.AccountState
import com.github.xs93.wan.common.router.PageRouterPath
import com.github.xs93.wan.common.store.AppCommonStore
import com.github.xs93.wan.common.viewmodel.MainAction
import com.github.xs93.wan.common.viewmodel.MainEvent
import com.github.xs93.wan.common.viewmodel.MainViewModel
import com.github.xs93.wan.main.R
import com.github.xs93.wan.main.databinding.ActivityMainBinding
import com.therouter.TheRouter
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint
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
@Route(path = PageRouterPath.ACTIVITY_MAIN)
class MainActivity : BaseVBActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var accountDataManager: AccountDataManager

    private var windowController: WindowInsetsControllerCompat? = null

    override fun initView(savedInstanceState: Bundle?) {
        windowController = WindowCompat.getInsetsController(window, window.decorView)

        viewBinding.apply {
            with(drawerRoot) {
                addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                    override fun onDrawerOpened(drawerView: View) {
                        if (drawerView.id == R.id.main_drawer_layout) {
                            windowController?.isAppearanceLightStatusBars = false
                        }
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        if (drawerView.id == R.id.main_drawer_layout) {
                            windowController?.isAppearanceLightStatusBars = !isNightMode
                        }
                    }
                })
            }
        }

        viewBinding.mainContentLayout.apply {
            with(vpContent) {
                offscreenPageLimit = 3
                setTouchSlopMultiple(2f)
                adapter = SimpleViewPagerAdapter(supportFragmentManager, lifecycle).apply {
                    add { TheRouter.build(PageRouterPath.FRAGMENT_HOME).createFragment()!! }
                    add { TheRouter.build(PageRouterPath.FRAGMENT_NAVIGATOR).createFragment()!! }
                    add { TheRouter.build(PageRouterPath.FRAGMENT_CLASSIFY).createFragment()!! }
                    add { TheRouter.build(PageRouterPath.FRAGMENT_MINE).createFragment()!! }
                }
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        bottomNavView.menu[position].isChecked = true
                    }
                })
            }

            with(bottomNavView) {
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.menu_home -> vpContent.setCurrentItem(0, false)
                        R.id.menu_navigator -> vpContent.setCurrentItem(1, false)
                        R.id.menu_classify -> vpContent.setCurrentItem(2, false)
                        R.id.menu_mine -> vpContent.setCurrentItem(3, false)
                    }
                    true
                }
            }
        }


        viewBinding.mainDrawerLayout.apply {
            with(btnLogin) {
                setSingleClickListener {
//                    startActivitySafe<LoginActivity>()
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
                    viewBinding.drawerRoot.openDrawer(GravityCompat.START)
                }
            }
        }

        observerState(accountDataManager.accountStateFlow) {
            when (it) {
                AccountState.LogOut -> {
                    with(viewBinding.mainDrawerLayout) {
                        btnLogin.visible()
                        btnLogout.gone()
                        groupUserInfo.gone()
                        imgAvatar.load(R.drawable.img_avatar_no_login)
                    }
                }

                is AccountState.LogIn -> {
                    with(viewBinding.mainDrawerLayout) {
                        btnLogin.gone()
                        btnLogout.visible()
                        groupUserInfo.visible()
                        imgAvatar.load(R.drawable.img_avatar_login)
                    }
                }
            }
        }

        observerState(accountDataManager.userDetailFlow) {
            with(viewBinding.mainDrawerLayout) {
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
        if (viewBinding.drawerRoot.isDrawerOpen(GravityCompat.START)) {
            windowController?.isAppearanceLightStatusBars = false
        }
    }
}