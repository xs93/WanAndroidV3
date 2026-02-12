package com.github.xs93.ui.ui.recyclerview

import androidx.viewbinding.ViewBinding

/**
 * 简单实现的BaseAdapter,方便使用VIewBinding
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/6 10:28
 * @email 466911254@qq.com
 */
abstract class SimpleAdapter<T : Any, VB : ViewBinding> : BaseAdapter<T, SimpleVH<VB>>()