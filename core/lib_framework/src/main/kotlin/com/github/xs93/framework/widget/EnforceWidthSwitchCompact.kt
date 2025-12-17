package com.github.xs93.framework.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.withStyledAttributes
import com.github.xs93.framework.R

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/8 17:40
 * @description 设置宽度的SwitchCompact
 *
 */
class EnforceWidthSwitchCompact @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwitchCompat(context, attrs) {

    private var enforceWidth: Boolean = false

    init {
        context.withStyledAttributes(attrs, R.styleable.EnforceWidthSwitchCompact) {
            enforceWidth = getBoolean(R.styleable.EnforceWidthSwitchCompact_enforce_width, false)
        }
        setEnforceSwitchWidth(enforceWidth)
    }
}