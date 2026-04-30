package com.github.xs93.wan.common.diff

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.github.xs93.wan.model.entity.Article

/**
 * 文章信息对象 ItemCallback实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 15:53
 * @email 466911254@qq.com
 */
class ArticleItemCallback : DiffUtil.ItemCallback<Article>() {

    companion object {
        const val PAYLOAD_KEY_COLLECT_STATE = "collect_state"
    }

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.collect == newItem.collect
    }

    override fun getChangePayload(oldItem: Article, newItem: Article): Any? {
        val bundle = Bundle()
        if (oldItem.collect != newItem.collect) {
            bundle.putBoolean(PAYLOAD_KEY_COLLECT_STATE, newItem.collect)
        }
        if (bundle.isEmpty) {
            return null
        }
        return bundle
    }
}