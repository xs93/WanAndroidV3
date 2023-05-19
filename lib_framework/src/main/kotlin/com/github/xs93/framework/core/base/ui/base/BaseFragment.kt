package com.github.xs93.framework.core.base.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.xs93.framework.core.ktx.setOnInsertsChangedListener
import com.github.xs93.framework.core.loading.IUiLoadingDialog
import com.github.xs93.framework.core.loading.IUiLoadingDialogProxy
import com.github.xs93.framework.core.toast.IToast
import com.github.xs93.framework.core.toast.UiToastProxy
import com.github.xs93.framework.core.ui.Surface

/**
 * 基础Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:25
 */
abstract class BaseFragment : Fragment(), IToast by UiToastProxy(), IUiLoadingDialog {


    private val mIUiLoadingDialog by lazy {
        IUiLoadingDialogProxy(childFragmentManager, viewLifecycleOwner)
    }

    protected val surface = Surface()


    private var mLazyLoad = false

    /**
     * 当前Fragment是否对用户可见
     */
    private var mIsVisibleToUser = false

    /**
     * 是否调用了setUserVisibleHint方法。处理show+add+hide模式下，默认可见 Fragment 不调用
     * onHiddenChanged 方法，进而不执行懒加载方法的问题。
     */
    private var mIsCallUserVisibleHint = false

    /**
     * 当使用ViewPager+Fragment形式会调用该方法时，setUserVisibleHint会优先Fragment生命周期函数调用，
     * 所以这个时候就,会导致在setUserVisibleHint方法执行时就执行了懒加载，
     * 而不是在onResume方法实际调用的时候执行懒加载。所以需要这个变量
     */
    private var mIsCallResume = false

    /**
     * 是否初始化View
     */
    private var mInitView: Boolean = false

    /**
     * 当前是否在OnResume生命周期范围
     */
    private var mResume: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getContentLayoutId() != 0) {
            return inflater.inflate(getContentLayoutId(), container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnInsertsChangedListener {
            surface.insets = it
        }
        beforeInitView(view, savedInstanceState)
        initView(view, savedInstanceState)
        mInitView = true
        initObserver(savedInstanceState)
        beforeInitData(savedInstanceState)
        initData(savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        lazyLoad()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mIsVisibleToUser = !hidden
        lazyLoad()
    }

    override fun onResume() {
        super.onResume()
        mResume = true
        mIsCallResume = true
        if (!mIsCallUserVisibleHint) {
            mIsVisibleToUser = !isHidden
        }
        lazyLoad()
    }

    override fun onPause() {
        super.onPause()
        mResume = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mInitView = false
        mIsVisibleToUser = false
        mIsCallResume = false
        mIsCallUserVisibleHint = false
    }

    private fun lazyLoad() {
        if (mIsVisibleToUser && mInitView && !mLazyLoad && mIsCallResume) {
            mLazyLoad = true
            onFirstVisible()
        }
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int


    open fun beforeInitView(view: View, savedInstanceState: Bundle?) {}

    /** 初始化View */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    open fun beforeInitData(savedInstanceState: Bundle?) {}

    /** 初始化订阅观察者 */
    open fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    open fun initData(savedInstanceState: Bundle?) {}

    /** 该fragment 第一次被显示时调用,可用作懒加载 */
    open fun onFirstVisible() {}

    /**
     * 是否在Resume显示期间
     *
     * @return true 在Resume期间
     */
    fun isResume(): Boolean {
        return mResume
    }

    /** 隐藏键盘 */
    protected fun hideKeyboardFrom(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun createLoadingDialog(): DialogFragment {
        return mIUiLoadingDialog.createLoadingDialog()
    }

    override fun showLoadingDialog(message: CharSequence?) {
        mIUiLoadingDialog.showLoadingDialog(message)
    }

    override fun updateLoadingDialog(message: CharSequence) {
        mIUiLoadingDialog.updateLoadingDialog(message)
    }

    override fun hideLoadingDialog() {
        mIUiLoadingDialog.hideLoadingDialog()
    }
}