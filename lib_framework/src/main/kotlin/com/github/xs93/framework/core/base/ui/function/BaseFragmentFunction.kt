package com.github.xs93.framework.core.base.ui.function

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * fragment 生命周期功能
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 10:09
 */
open class BaseFragmentFunction {
    open fun onAttached(manager: FragmentManager, fragment: Fragment, context: Context) {}

    open fun onCreated(manager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {}

    open fun onViewCreated(manager: FragmentManager, fragment: Fragment, iew: View, savedInstanceState: Bundle?) {}

    open fun onStarted(manager: FragmentManager, fragment: Fragment) {}

    open fun onResumed(manager: FragmentManager, fragment: Fragment) {}

    open fun onPaused(manager: FragmentManager, fragment: Fragment) {}

    open fun onStopped(manager: FragmentManager, fragment: Fragment) {}

    open fun onViewDestroyed(manager: FragmentManager, fragment: Fragment) {}

    open fun onDestroyed(manager: FragmentManager, fragment: Fragment) {}

    open fun onDetached(manager: FragmentManager, fragment: Fragment) {}

    open fun onSaveInstanceStated(manager: FragmentManager, fragment: Fragment, outState: Bundle) {}

    open fun setUserVisibleHinted(manager: FragmentManager, fragment: Fragment, isVisibleToUser: Boolean) {}
}