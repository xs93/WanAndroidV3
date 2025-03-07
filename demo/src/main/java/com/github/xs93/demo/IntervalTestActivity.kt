package com.github.xs93.demo

import android.os.Bundle
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityIntervalTestBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ui.ContentPadding
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
class IntervalTestActivity : BaseViewBindingActivity<ActivityIntervalTestBinding>(
    R.layout.activity_interval_test,
    ActivityIntervalTestBinding::bind
) {

    private var interval: Interval? = null

    override fun initView(savedInstanceState: Bundle?) {
        binding.btnStart.setSingleClickListener {
            if (interval == null) {
                interval = Interval(0, 1, TimeUnit.SECONDS, 50)
            }
            interval?.subscribe {
                binding.tvTime.text = it.toString()
            }?.finish {
                binding.tvTime.text = "finish"
            }?.life(this)
                ?.onlyResumed(this)
                ?.start()
        }

        binding.btnStop.setSingleClickListener {
            interval?.cancel()
        }
    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.root.updatePadding(top = contentPadding.top, bottom = contentPadding.bottom)
    }
}