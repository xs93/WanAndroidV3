package com.github.xs93.wanandroid.common.rv

import androidx.recyclerview.widget.DiffUtil
import com.github.xs93.wanandroid.common.model.Article

/**
 * Article DiffCallback 实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/2 10:52
 * @email 466911254@qq.com
 */
class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {

    companion object {
        const val PAYLOAD_COLLECT = "payload_collect"
    }

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.collect == newItem.collect
    }

    override fun getChangePayload(oldItem: Article, newItem: Article): Any? {
        return if (oldItem.collect != newItem.collect) {
            PAYLOAD_COLLECT
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}