package com.github.xs93.framework.widget

import android.content.Context
import android.util.DisplayMetrics
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Method

/**
 * 反射设置ViewPager2 item滚动时间
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/6 11:44
 * @email 466911254@qq.com
 */
class ViewPager2SlowScrollHelper(private val vp: ViewPager2, val duration: Long) {

    private val recyclerView: RecyclerView
    private val mAccessibilityProvider: Any?
    private val mScrollEventAdapter: Any?
    private val onSetNewCurrentItemMethod: Method
    private val getRelativeScrollPositionMethod: Method
    private val notifyProgrammaticScrollMethod: Method

    init {
        val mRecyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        mRecyclerViewField.isAccessible = true
        recyclerView = mRecyclerViewField.get(vp) as RecyclerView
        val mAccessibilityProviderField = ViewPager2::class.java.getDeclaredField("mAccessibilityProvider")
        mAccessibilityProviderField.isAccessible = true
        mAccessibilityProvider = mAccessibilityProviderField.get(vp)

        onSetNewCurrentItemMethod = mAccessibilityProvider.javaClass.getDeclaredMethod("onSetNewCurrentItem")
        onSetNewCurrentItemMethod.isAccessible = true

        val mScrollEventAdapterField = ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
        mScrollEventAdapterField.isAccessible = true

        mScrollEventAdapter = mScrollEventAdapterField.get(vp)
        getRelativeScrollPositionMethod =
            mScrollEventAdapter.javaClass.getDeclaredMethod("getRelativeScrollPosition")
        getRelativeScrollPositionMethod.isAccessible = true

        notifyProgrammaticScrollMethod = mScrollEventAdapter.javaClass.getDeclaredMethod(
            "notifyProgrammaticScroll",
            Int::class.java,
            Boolean::class.java
        )
        notifyProgrammaticScrollMethod.isAccessible = true
    }

    /**
     * 模拟手写Viewpage2的setCurrentItemInternal(int item, boolean smoothScroll)方法
     * 其中smoothScroll为true
     * 主要目的是通过手动实现vp的翻页方法达到控制RecycleView执行滚动的SmoothScroller对象
     */
    fun setCurrentItem(item: Int) {
        var useItem = item
        val adapter: RecyclerView.Adapter<*> = vp.adapter as RecyclerView.Adapter<*>
        if (adapter.itemCount <= 0) {
            return
        }
        useItem = useItem.coerceAtLeast(0)
        useItem = useItem.coerceAtMost(adapter.itemCount - 1)
        if (useItem == vp.currentItem && vp.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
            return
        }
        if (useItem == vp.currentItem) {
            return
        }
        vp.currentItem = useItem
        onSetNewCurrentItemMethod.invoke(mAccessibilityProvider)
        notifyProgrammaticScrollMethod.invoke(mScrollEventAdapter, useItem, true)
        smoothScrollToPosition(useItem, vp.context, recyclerView.layoutManager)
    }

    /**
     * 模拟手写RecyclerView的smoothScrollToPosition方法 替换了startSmoothScroll的参数达到了改变速度的目的
     */
    private fun smoothScrollToPosition(
        item: Int,
        context: Context,
        layoutManager: RecyclerView.LayoutManager?
    ) {
        val linearSmoothScroller = getSlowLinearSmoothScroller(context)
        replaceDecelerateInterpolator(linearSmoothScroller)
        linearSmoothScroller.targetPosition = item
        layoutManager?.startSmoothScroll(linearSmoothScroller)
    }

    /**
     * 减速核心SmoothScroller对象，super.calculateSpeedPerPixel(displayMetrics) * slowCoefficient 为速度放慢slowCoefficient倍
     * 既动画时长增加slowCoefficient倍
     */
    private fun getSlowLinearSmoothScroller(context: Context): RecyclerView.SmoothScroller {
        return object : LinearSmoothScroller(context) {
            /**
             * ？？？？？？
             * ？？？？？？
             * 按照sdk注释的内容理解这个方法的返回值为每个像素滚动的时间 例如返回 1 则代表滚动1个像素需要1ms 既1920px的滚动距离 则需要滚动1.92s
             * 所以返回值应该是 duration/width 比如期望滚动1s 也就是需要返回 1000/vp.width
             * 但是根据实际测试 如果按照返回值是 duration/width来计算  当返回 duration/width = 1时 duration期望应该是with（假设with是1920px duration则是1920ms）但是实际duration约等于3倍with（1920px滚动5700ms ）？？？？
             * 暂无实际证据可以证实这个值是 3倍
             * 但是calculateSpeedPerPixel的返回值的确和sdk注释描述的是有出入的，暂时先用3作为调整系数
             * 也有可能是和我们设备相关 横屏 1920*1080 320dpi，使用的时候可以重新测试一下。
             * ？？？？？？
             * ？？？？？？
             */
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return duration / (vp.width.toFloat() * 3.0f)
            }
        }
    }

    /**
     * 修改SmoothScroller的默认差值器，将其改为线性输出，不然会影响后续的vp动画
     * 如果没有自定义动画可以不用这个方法
     */
    private fun replaceDecelerateInterpolator(linearSmoothScroller: RecyclerView.SmoothScroller) {
        val mDecelerateInterpolatorField =
            LinearSmoothScroller::class.java.getDeclaredField("mDecelerateInterpolator")
        mDecelerateInterpolatorField.isAccessible = true
        mDecelerateInterpolatorField.set(linearSmoothScroller, object : DecelerateInterpolator() {
            override fun getInterpolation(input: Float): Float {
                return input
            }
        })
    }
}