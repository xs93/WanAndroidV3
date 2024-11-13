package com.github.xs93.framework.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.xs93.framework.R
import kotlin.math.absoluteValue

/**
 * 模拟点击事件的 MotionEvent 向外传递，用于解决ViewPager2 下层的Ui点击事件失效的问题
 * 使用方法: 此Layout 作为ViewPager2 内部布局的根布局
 * 需要响应点击事件的Layout主动 dispatchTouchEvent 分发event
 *
 * 应用场景:直播间上层操作Ui使用ViewPager2+Fragment 做操作层和纯净模式，下层控件需要响应点击事件，但点击事件无法响应
 *
 * 触摸除了已经排除的View区域,则回调隐藏软键盘，实现触摸输入框外的其他区域软键盘收起功能
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/13 16:54
 * @email 466911254@qq.com
 */
class MockClickEventConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var touchSlop = 0
    private var tapTimeout = 0
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var touchDownTime: Long = 0L

    private var onMockClickEventListener: ((event: MotionEvent) -> Unit)? = null
    private var hideSoftKeyboard: (() -> Unit)? = null

    private var excludeIds: String? = null
    private var excludeViews: MutableList<View> = mutableListOf()

    init {
        touchSlop = ViewConfiguration.get(context).scaledDoubleTapSlop
        tapTimeout = ViewConfiguration.getTapTimeout()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MockClickEventConstraintLayout)
        excludeIds = ta.getString(R.styleable.MockClickEventConstraintLayout_exclude_child_view_ids)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initExcludeView()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        ViewConfiguration.get(context).scaledTouchSlop
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.rawX
                startY = event.rawY
                touchDownTime = SystemClock.elapsedRealtime()
            }

            MotionEvent.ACTION_UP -> {
                val touchTime = SystemClock.elapsedRealtime() - touchDownTime
                val moveX = event.rawX - startX
                val moveY = event.rawY - startY

                if (moveX.absoluteValue < touchSlop && moveY.absoluteValue < touchSlop && touchTime < tapTimeout) {
                    var downTime = SystemClock.uptimeMillis()
                    val mockDownEvent = MotionEvent.obtain(
                        downTime,
                        downTime,
                        MotionEvent.ACTION_DOWN,
                        startX,
                        startY,
                        0
                    )
                    onMockClickEventListener?.invoke(mockDownEvent)
                    mockDownEvent.recycle()

                    downTime += tapTimeout
                    val mockUpEvent = MotionEvent.obtain(
                        downTime,
                        downTime,
                        MotionEvent.ACTION_UP,
                        startX,
                        startY,
                        0
                    )
                    onMockClickEventListener?.invoke(mockUpEvent)
                    mockUpEvent.recycle()
                }
                if (!isExcludeView(startX, startY)) {
                    hideSoftKeyboard?.invoke()
                }
            }
        }
        return true
    }


    fun setOnMockClickEventListener(listener: ((event: MotionEvent) -> Unit)? = null) {
        onMockClickEventListener = listener
    }

    fun setHideSoftKeyboard(block: () -> Unit) {
        hideSoftKeyboard = block
    }


    private fun initExcludeView() {
        excludeViews.clear()
        if (excludeIds.isNullOrBlank()) return
        val idsStr = excludeIds?.split(",")
        idsStr?.forEach { idStr ->
            val id = resources.getIdentifier(idStr, "id", context.packageName)
            if (id != 0) {
                val childView = findViewById<View>(id)
                if (childView != null) {
                    excludeViews.add(childView)
                }
            }
        }
    }

    private fun isExcludeView(x: Float, y: Float): Boolean {
        return excludeViews.any { view -> isInsideView(view, x, y) }
    }

    private fun isInsideView(view: View, x: Float, y: Float): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height
        return x >= left && x <= right && y >= top && y <= bottom
    }
}