package com.github.xs93.wanandroid.main

import android.os.Bundle
import android.view.LayoutInflater
import com.github.xs93.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.wanandroid.main.databinding.ActivityMainBinding

/**
 * 首页界面
 *
 *
 * @author xushuai
 * @date   2022/9/3-11:19
 * @email  466911254@qq.com
 */
class MainActivity : BaseVbActivity<ActivityMainBinding>() {


    override fun createViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}