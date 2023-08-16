package com.github.xs93.framework.base.ui.function

import android.content.Context
import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.base.BaseBottomSheetDialogControlFragment

/**
 * 添加方法基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 9:40
 * @email 466911254@qq.com
 */
abstract class BaseFunctionBottomSheetDialogFragmentControl :
    BaseBottomSheetDialogControlFragment() {


    private val functions: MutableList<BaseFragmentFunction> =
        FunctionsManager.createFragmentFunctions()

    override fun onAttach(context: Context) {
        addFunctions()
        super.onAttach(context)
        for (function in functions) {
            function.onAttached(parentFragmentManager, this, context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (function in functions) {
            function.onCreated(parentFragmentManager, this, savedInstanceState)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (function in functions) {
            function.onViewCreated(parentFragmentManager, this, view, savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        for (function in functions) {
            function.onStarted(parentFragmentManager, this)
        }
    }

    override fun onResume() {
        super.onResume()
        for (function in functions) {
            function.onResumed(parentFragmentManager, this)
        }
    }

    override fun onPause() {
        super.onPause()
        for (function in functions) {
            function.onPaused(parentFragmentManager, this)
        }
    }

    override fun onStop() {
        super.onStop()
        for (function in functions) {
            function.onStopped(parentFragmentManager, this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        for (function in functions) {
            function.onViewDestroyed(parentFragmentManager, this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        for (function in functions) {
            function.onDestroyed(parentFragmentManager, this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        for (function in functions) {
            function.onDetached(parentFragmentManager, this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        for (function in functions) {
            function.onSaveInstanceStated(parentFragmentManager, this, outState)
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        for (function in functions) {
            function.setUserVisibleHinted(parentFragmentManager, this, isVisibleToUser)
        }
    }

    /**
     * 添加界面单独的功能
     */
    open fun addFunctions() {

    }

    fun addFunction(function: BaseFragmentFunction) {
        functions.add(function)
    }
}