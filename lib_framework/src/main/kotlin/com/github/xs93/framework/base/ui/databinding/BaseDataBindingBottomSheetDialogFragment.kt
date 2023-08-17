package com.github.xs93.framework.base.ui.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionBottomSheetDialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 9:42
 * @email 466911254@qq.com
 */
abstract class BaseDataBindingBottomSheetDialogFragment<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    BaseFunctionBottomSheetDialogFragment() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return layoutId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, getContentLayoutId(), container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}