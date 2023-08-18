package com.github.xs93.wanandroid.common.diff

import androidx.recyclerview.widget.DiffUtil
import com.github.xs93.wanandroid.common.entity.Article

/**
 * 文章信息对象 ItemCallback实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 15:53
 * @email 466911254@qq.com
 */
class ArticleItemCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.collect == newItem.collect
    }
}