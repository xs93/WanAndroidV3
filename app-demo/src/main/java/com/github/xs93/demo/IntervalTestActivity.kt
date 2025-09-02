package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityIntervalTestBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.interval.Interval
import com.github.xs93.utils.ktx.setSingleClickListener
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/7 15:02
 * @email 466911254@qq.com
 */
class IntervalTestActivity : BaseVBActivity<ActivityIntervalTestBinding>(
    ActivityIntervalTestBinding::inflate
) {

    private var interval: Interval? = null

    override fun initView(savedInstanceState: Bundle?) {
        viewBinding.btnStart.setSingleClickListener {
            if (interval == null) {
                interval = Interval(0, 1, TimeUnit.SECONDS, 50)
            }
            interval?.subscribe {
                viewBinding.tvTime.text = it.toString()
            }?.finish {
                viewBinding.tvTime.text = "finish"
            }?.life(this)
                ?.onlyResumed(this)
                ?.start()
        }

        viewBinding.btnStop.setSingleClickListener {
            interval?.cancel()
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        viewBinding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}