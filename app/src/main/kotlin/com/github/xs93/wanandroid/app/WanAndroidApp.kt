package com.github.xs93.wanandroid.app

import com.almightyai.robot.coil.CoilManager
import com.github.xs93.common.R
import com.github.xs93.framework.base.ui.utils.BaseDialogFragmentConfig
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.exception.ServiceApiException
import com.github.xs93.utils.net.NetworkMonitor
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.CommonApplication
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 10:11
 * @email 466911254@qq.com
 */
@HiltAndroidApp
class WanAndroidApp : CommonApplication() {

    override fun onCreate() {
        super.onCreate()

        CoilManager.init(this)
        initHttp()
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            BallPulseFooter(context)
        }
        BaseDialogFragmentConfig.commonBottomSheetDialogTheme =
            com.github.xs93.wanandroid.app.R.style.AppBottomSheetDialog
    }


    private fun initHttp() {
        NetworkMonitor.init(this)
        EasyRetrofit.init(this) {
            if (it is ServiceApiException) {
                ToastManager.showToast(it.errorMsg)
            } else {
                ToastManager.showToast(R.string.network_error)
            }
        }
        EasyRetrofit.addRetrofitClient(AppConstant.BaseUrl)
    }
}