package com.github.xs93.framework.base.ui.interfaces

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.github.xs93.framework.ui.ContentPadding

/**
 * Fragment基类封装方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 15:49
 * @email 466911254@qq.com
 */
interface IBaseFragment {

    /**返回布局Layout*/
    @LayoutRes
    fun getContentLayoutId(): Int

    /** 初始化View */
    fun initView(view: View, savedInstanceState: Bundle?)

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