package com.github.xs93.framework.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/6 10:31
 * @email 466911254@qq.com
 */
class SimpleVH<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)