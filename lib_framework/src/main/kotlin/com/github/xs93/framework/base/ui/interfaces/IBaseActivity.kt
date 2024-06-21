package com.github.xs93.framework.base.ui.interfaces

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.github.xs93.framework.ui.ContentPadding

/**
 * Activity基类封装方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 15:41
 * @email 466911254@qq.com
 */
interface IBaseActivity {

    /**执行在super.onCreate(savedInstanceState)之前*/
    fun beforeSuperOnCreate(savedInstanceState: Bundle?) {}

    /**在setContentView之前执行*/
    fun beforeSetContentView(savedInstanceState: Bundle?) {}

    /**返回布局Layout*/
    @LayoutRes
    fun getContentLayoutId(): Int

    /**返回ContentView*/
    fun getContentView(): View?

    /** 自定义如何去调用SetContentView方法，调用此方法前提是 getContentLayoutId ==0 并且 getContentView ==null */
    fun customSetContentView() {}

    /**初始化View*/
    fun initView(savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    fun initData(savedInstanceState: Bundle?) {}

    /**
     * 系统UI的宽高变更
     * @param contentPadding ContentPadding 系统Ui边距
     */
    fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {}
}