package com.github.xs93.wan.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseDifferAdapter
import com.github.xs93.utils.ktx.gone
import com.github.xs93.utils.ktx.toHtml
import com.github.xs93.utils.ktx.visible
import com.github.xs93.wan.common.R
import com.github.xs93.wan.common.databinding.CommonItemArticleBinding
import com.github.xs93.wan.common.diff.ArticleItemCallback
import com.github.xs93.wan.common.diff.ArticleItemChangePayload
import com.github.xs93.wan.common.diff.ArticleItemChanged
import com.github.xs93.wan.data.entity.Article

/**
 *
 * 首页文章列表适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 15:55
 * @email 466911254@qq.com
 */
class CommonArticleAdapter :
    BaseDifferAdapter<Article, CommonArticleAdapter.ArticleViewHolder>(ArticleItemCallback()) {

    class ArticleViewHolder(val bind: CommonItemArticleBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = CommonItemArticleBinding.inflate(layoutInflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int, item: Article?) {
        with(holder.bind) {
            item?.let {
                txtAuthor.text = it.showAuthorName
                if (it.isTop) {
                    txtIsTop.visible()
                } else {
                    txtIsTop.gone()
                }
                if (it.fresh) {
                    txtNew.visible()
                } else {
                    txtNew.gone()
                }
                if (it.tags.isNotEmpty()) {
                    txtTag1.visible()
                    txtTag1.text = it.tags[0].name
                } else {
                    txtTag1.gone()
                }
                if (it.tags.size > 1) {
                    txtTag2.visible()
                    txtTag2.text = it.tags[1].name
                } else {
                    txtTag2.gone()
                }
                txtDate.text = it.niceDate
                txtTitle.text = it.title.toHtml()
                txtType.text = context.getString(
                    R.string.common_article_chapter_name,
                    it.superChapterName,
                    it.chapterName
                )
                imgCollect.isChecked = it.collect
            }
        }
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int,
        item: Article?,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is ArticleItemChanged) {
                    it.payloads.forEach { payload ->
                        when (payload) {
                            ArticleItemChangePayload.CollectStateChanged -> {
                                if (item != null) {
                                    holder.bind.imgCollect.isChecked = item.collect
                                }
                            }
                        }
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, item, payloads)
        }
    }
}