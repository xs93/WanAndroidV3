package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.github.xs93.demo.databinding.ActivityIntervalTestBinding
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.interval.Interval
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.net.KNetwork
import kotlinx.coroutines.launch
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
        vBinding.btnStart.setSingleClickListener {
            if (interval == null) {
                interval = Interval(0, 1, TimeUnit.SECONDS, 50)
            }
            interval?.subscribe {
                vBinding.tvTime.text = it.toString()
            }?.finish {
                vBinding.tvTime.text = "finish"
            }?.life(this)
                ?.onlyResumed(this)
                ?.start()
        }
        vBinding.btnStop.setSingleClickListener {
            interval?.cancel()
        }

        vBinding.btnNetworkState.setSingleClickListener {
            KNetwork.init(this)
            lifecycleScope.launch {
                KNetwork.networkFlow.collect {
                    vBinding.tvCurrentNetwork.text = it.toString()
                }
            }
            lifecycleScope.launch {
                KNetwork.networksFlow.collect {
                    vBinding.tvAllNetwork.text = it.toString()
                }
            }
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        vBinding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}