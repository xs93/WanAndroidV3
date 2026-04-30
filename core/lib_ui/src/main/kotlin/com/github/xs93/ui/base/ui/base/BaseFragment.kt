package com.github.xs93.ui.base.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.xs93.ui.base.ui.base.interfaces.IFragment
import com.github.xs93.ui.base.ui.base.interfaces.IWindowInsetsListener

/**
 * 基础Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:25
 */
abstract class BaseFragment : Fragment(), IFragment, IWindowInsetsListener {

    private var mFirstVisibleCalled: Boolean = false
    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (contentLayoutId != 0) {
            return inflater.inflate(contentLayoutId, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        windowInsetsHelper.attach(view, this)
        initParams(arguments)
        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mFirstVisibleCalled) { //只有为true 才保存状态,减少状态保存
            outState.putBoolean("FIRST_VISIBLE_CALLED", true)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mFirstVisibleCalled = savedInstanceState?.getBoolean("FIRST_VISIBLE_CALLED") == true
    }

    override fun onResume() {
        super.onResume()
        if (!mFirstVisibleCalled) {
            onFirstVisible()
            mFirstVisibleCalled = true
        }
    }

    /** 该fragment 第一次被显示时调用,可用作懒加载 */
    protected open fun onFirstVisible() {}

    /**
     * onFirstVisible是否被调用过
     * @return Boolean true 被调用过,false没有被调用过
     */
    fun firstVisibleCalled(): Boolean = mFirstVisibleCalled
}