package com.github.xs93.wan.common.ui.viewbinding

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 使用ViewBinding 的ViewHolder
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/8 11:00
 * @email 466911254@qq.com
 */
class BaseVBViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)