package com.github.xs93.framework.core.base.ui.vbvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.xs93.framework.core.base.ui.viewbinding.BaseVbDialogFragment
import com.github.xs93.framework.core.base.viewmodel.BaseViewModel
import com.github.xs93.framework.core.base.viewmodel.CommonUiEvent
import com.github.xs93.framework.core.ktx.repeatOnStarted
import com.github.xs93.framework.core.utils.ClassUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.Modifier

/**
 * 可以使用ViewBinding,dataBinding,和ViewModel的DialogFragment
 *
 *
 * @author xushuai
 * @date   2022/5/15-15:41
 * @email  466911254@qq.com
 */
abstract class BaseVbVmDialogFragment<VB : ViewDataBinding, VM : BaseViewModel<*, *, *>>(@LayoutRes layoutId: Int) :
    BaseVbDialogFragment<VB>(layoutId) {

    /** 泛型中的默认ViewModel对象 */
    protected lateinit var viewModel: VM

    override fun beforeInitView(view: View, savedInstanceState: Bundle?) {
        super.beforeInitView(view, savedInstanceState)
        val vm = createViewModel()
        viewModel = if (vm != null) {
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))[vm::class.java]
        } else {
            val clazz: Class<VM>? = ClassUtils.getGenericClassByClass(this, ViewModel::class.java)
            if (clazz == null || clazz == ViewModel::class.java) {
                return
            }
            //判断此VM是否有abstract标记,有则无法初始化
            val isAbstract = Modifier.isAbstract(clazz.modifiers)
            if (isAbstract) {
                return
            }
            ViewModelProvider(this)[clazz]
        }
    }

    /**
     * 创建ViewModel对象,直接new 一个对象供ViewModelFactory使用，如果返回null，则尝试通过反射初始化无参数ViewModel
     * @return VM 泛型的ViewModel对象,
     */
    protected open fun createViewModel(): VM? {
        return null
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        viewModel.commonEventFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is CommonUiEvent.ShowLoadingDialog -> {
                        showLoadingDialog(it.message)
                    }

                    is CommonUiEvent.UpdateLoadingDialog -> {
                        updateLoadingDialog(it.message)
                    }

                    CommonUiEvent.HideLoadingDialog -> {
                        hideLoadingDialog()
                    }

                    is CommonUiEvent.ShowToast -> {
                        showToast(it.charSequence, it.duration)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}