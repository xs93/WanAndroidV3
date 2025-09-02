package com.github.xs93.camera

import android.content.Context
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.core.ZoomState
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/25$ 14:21$
 * @description 相机使用封装
 *
 */
class CameraHelper(
    private val context: Context,
    private val previewView: PreviewView,
    private val lifecycleOwner: LifecycleOwner
) {

    companion object {
        private const val FACING_BACK = 1
        private const val FACING_FRONT = 2

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private lateinit var mCameraExecutor: ExecutorService

    private lateinit var mCameraProvider: ProcessCameraProvider
    private lateinit var mCameraSelector: CameraSelector
    private lateinit var mCamera: Camera
    private lateinit var mPreView: Preview //预览
    private lateinit var mImageCapture: ImageCapture //拍照

    private var mCameraFacing = FACING_BACK
    private var mCameraInitialized: Boolean = false

    private val mCameraLifecycleOwner by lazy {
        CameraLifecycleOwner()
    }

    private val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) {
                return
            }
            val rotation = when (orientation) {
                in 45 until 135 -> Surface.ROTATION_270
                in 135 until 225 -> Surface.ROTATION_180
                in 225 until 315 -> Surface.ROTATION_90
                else -> Surface.ROTATION_0
            }
            if (mCameraInitialized) {
                mImageCapture.targetRotation = rotation
            }
        }
    }


    var cameraChange: ((Camera) -> Unit)? = null
    var cameraZoomStateChange: ((ZoomState) -> Unit)? = null


    fun startCamera(callback: (Boolean) -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                mCameraProvider = cameraProviderFuture.get()
                previewView.post {
                    bindCamera(callback)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(false)
            }
        }, ContextCompat.getMainExecutor(context))

        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        mCameraExecutor = Executors.newSingleThreadExecutor()
                        mCameraLifecycleOwner.cameraOnCreate()
                    }

                    Lifecycle.Event.ON_START -> {
                        mCameraLifecycleOwner.cameraOnStart()
                        orientationEventListener.enable()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        mCameraLifecycleOwner.cameraOnResume()
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        mCameraLifecycleOwner.cameraOnPause()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        mCameraLifecycleOwner.cameraOnStop()
                        orientationEventListener.disable()
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        mCameraLifecycleOwner.cameraOnDestroyed()
                        mCameraExecutor.shutdown()
                        source.lifecycle.removeObserver(this)
                    }

                    Lifecycle.Event.ON_ANY -> {
                        mCameraLifecycleOwner.cameraOnAny()
                    }
                }
            }
        })
    }


    private fun setupCameraUserCase() {
        mPreView = Preview.Builder().build()
            .also { it.surfaceProvider = previewView.surfaceProvider }

        mImageCapture = ImageCapture.Builder()
            .setFlashMode(FLASH_MODE_OFF)
            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        setupCameraSelector()
    }

    private fun bindCamera(callback: ((Boolean) -> Unit)?) {
        try {
            val screenAspectRatio = aspectRatio(previewView.width, previewView.height)
            val aspectRatioStrategy =
                AspectRatioStrategy(screenAspectRatio, AspectRatioStrategy.FALLBACK_RULE_AUTO)
            val resolutionSelector =
                ResolutionSelector.Builder().setAspectRatioStrategy(aspectRatioStrategy).build()

            val rotation = previewView.display.rotation

            mPreView = Preview.Builder()
                .setTargetRotation(rotation)
                .setResolutionSelector(resolutionSelector)
                .build()
                .also { it.surfaceProvider = previewView.surfaceProvider }

            mImageCapture = ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(rotation)
                .setResolutionSelector(resolutionSelector)
                .build()

            setupCameraSelector()

            mCameraProvider.unbindAll()
            mCamera = mCameraProvider.bindToLifecycle(
                mCameraLifecycleOwner,
                mCameraSelector,
                mImageCapture,
                mPreView
            )
            setupCameraListener(mCamera)
            mCameraInitialized = true
            callback?.invoke(true)
        } catch (e: Exception) {
            e.printStackTrace()
            mCameraInitialized = false
            callback?.invoke(false)
        }
    }

    private fun setupCameraListener(camera: Camera) {
        cameraChange?.invoke(mCamera)
        camera.cameraInfo.zoomState.observe(mCameraLifecycleOwner) {
            cameraZoomStateChange?.invoke(it)
        }
    }


    private fun setupCameraSelector() {
        mCameraSelector = if (mCameraFacing == FACING_BACK) {
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        } else {
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
        }
    }

    fun getCameraInfo(): CameraInfo? {
        if (mCameraInitialized) {
            return mCamera.cameraInfo
        }
        return null
    }

    fun toggleFacingBackCamera() {
        switchCameraFacing(FACING_BACK)
    }

    fun toggleFacingFrontCamera() {
        switchCameraFacing(FACING_FRONT)
    }

    private fun switchCameraFacing(cameraFacing: Int) {
        mCameraFacing = if (cameraFacing == FACING_BACK) {
            FACING_BACK
        } else {
            FACING_FRONT
        }
        setupCameraSelector()
        if (!mCameraInitialized) {
            return
        }
        bindCamera(null)
    }


    fun setLinearZoom(ratio: Float) {
        val linearZoom = if (ratio <= 0f) {
            0f
        } else if (ratio >= 1.0f) {
            1.0f
        } else {
            ratio
        }
        if (!mCameraInitialized) {
            return
        }
        mCamera.cameraControl.setLinearZoom(linearZoom)
    }

    fun setZoomRation(ratio: Float) {
        if (!mCameraInitialized) {
            return
        }
        try {
            mCamera.cameraControl.setZoomRatio(ratio)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun openTorch(open: Boolean) {
        if (!mCameraInitialized) {
            return
        }
        val cameraInfo = mCamera.cameraInfo
        if (!cameraInfo.hasFlashUnit()) {
            return
        }
        mCamera.cameraControl.enableTorch(open)
    }

    fun enableTorch() {
        if (!mCameraInitialized) {
            return
        }
        val cameraInfo = mCamera.cameraInfo
        if (!cameraInfo.hasFlashUnit()) {
            return
        }
        if (cameraInfo.torchState.value == TorchState.OFF) {
            mCamera.cameraControl.enableTorch(true)
        } else {
            mCamera.cameraControl.enableTorch(false)
        }
    }


    fun setExposureCompensationIndex(index: Int) {
        if (!mCameraInitialized) {
            return
        }
        if (!mCamera.cameraInfo.exposureState.isExposureCompensationSupported) {
            return
        }
        try {
            mCamera.cameraControl.setExposureCompensationIndex(index)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun takePhoto(
        saveFilePath: String,
        success: (path: String) -> Unit,
        error: (message: String) -> Unit
    ) {
        if (!mCameraInitialized) {
            error.invoke(context.getString(R.string.camera_not_available))
            return
        }
        val meteData = ImageCapture.Metadata().apply {
            isReversedHorizontal = mCameraFacing == FACING_FRONT
        }
        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(File(saveFilePath))
            .setMetadata(meteData)
            .build()

        mImageCapture.takePicture(
            outputFileOptions,
            mCameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val saveUri = outputFileResults.savedUri
                    if (saveUri == null) {
                        error.invoke(context.getString(R.string.camera_save_error))
                    } else {
                        success.invoke(saveUri.toFile().absolutePath)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    error.invoke(context.getString(R.string.camera_save_error))
                }
            })
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun logD(message: String) {
        Log.d("CameraHelper", message)
    }
}