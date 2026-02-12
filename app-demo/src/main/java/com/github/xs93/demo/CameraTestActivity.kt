package com.github.xs93.demo

import android.os.Bundle
import android.util.Log
import com.github.xs93.camera.CameraHelper
import com.github.xs93.demo.databinding.ActivityCameraTestBinding
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/25$ 14:53$
 * @description 相机测试Activity
 *
 */
class CameraTestActivity : BaseVBActivity<ActivityCameraTestBinding>(
    ActivityCameraTestBinding::inflate
) {
    companion object {
        private const val TAG = "CameraTestActivity"
    }

    private lateinit var mCameraHelper: CameraHelper

    override fun initView(savedInstanceState: Bundle?) {
        mCameraHelper = CameraHelper(this, vBinding.previewView, this)
        mCameraHelper.apply {
            cameraChange = {

            }

            cameraZoomStateChange = {

            }
        }
        mCameraHelper.startCamera {
            Log.d(TAG, "startCamera:$it ")
        }
    }
}