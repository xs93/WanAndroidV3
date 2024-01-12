package com.github.xs93.wanandroid.app.ui.home.child.square

import android.os.Bundle
import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.utils.ktx.dp
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.SquareFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.orhanobut.logger.Logger

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 14:30
 * @email 466911254@qq.com
 */
class SquareFragment : BaseViewBindingFragment<SquareFragmentBinding>(
    R.layout.square_fragment,
    SquareFragmentBinding::bind
) {

    companion object {

        fun newInstance(): SquareFragment {
            val args = Bundle()
            val fragment = SquareFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var mBottomBehavior: BottomSheetBehavior<NestedScrollView>

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBottomBehavior = BottomSheetBehavior.from(binding.scrollView)
        mBottomBehavior.isHideable = true
        mBottomBehavior.peekHeight = 400.dp(requireContext()).toInt()
        mBottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Logger.d("${bottomSheet},$newState")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset < 0) {
                    val heightOffset = (1 + slideOffset) * mBottomBehavior.peekHeight
                    binding.content.updateLayoutParams {
                        height = (binding.root.height - heightOffset).toInt()
                        Logger.d("$height,$heightOffset")
                    }
                }

            }
        })
        mBottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        // Handler(Looper.getMainLooper()).postDelayed(3000) {
        //     mBottomBehavior.isHideable = false
        // }
    }
}