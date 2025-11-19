package com.github.xs93.framework.base.ui.interfaces

import android.os.Bundle
import androidx.annotation.LayoutRes

/**
 * Activity基类封装方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 15:41
 * @email 466911254@qq.com
 */
interface IActivity {

    /**布局Layout*/
    @get:LayoutRes
    val contentLayoutId: Int

    /**执行在super.onCreate(savedInstanceState)之前*/
    fun beforeSuperOnCreate(savedInstanceState: Bundle?) {}

    /**在setContentView之前执行*/
    fun beforeSetContentView(savedInstanceState: Bundle?) {}

    /** contentLayoutId==0 则执行此方法 */
    fun customSetContentView() {}

    /**初始化View*/
    fun initView(savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    fun initData(savedInstanceState: Bundle?) {}
}