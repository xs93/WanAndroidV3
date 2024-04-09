package com.github.xs93.wanandroid.app.ui.home.child

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseDifferAdapter
import com.github.xs93.utils.ktx.gone
import com.github.xs93.utils.ktx.toHtml
import com.github.xs93.utils.ktx.visible
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.HomeArticleItemBinding
import com.github.xs93.wanandroid.common.diff.ArticleItemCallback
import com.github.xs93.wanandroid.common.diff.ArticleItemChangePayload
import com.github.xs93.wanandroid.common.diff.ArticleItemChanged
import com.github.xs93.wanandroid.common.entity.Article

/**
 *
 * 首页文章列表适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 15:55
 * @email 466911254@qq.com
 */
class HomeArticleAdapter : BaseDifferAdapter<Article, HomeArticleAdapter.HomeArticleViewHolder>(ArticleItemCallback()) {

    class HomeArticleViewHolder(val bind: HomeArticleItemBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): HomeArticleViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = HomeArticleItemBinding.inflate(layoutInflater, parent, false)
        return HomeArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int, item: Article?) {
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
                txtType.text = context.getString(R.string.article_chapter_name, it.superChapterName, it.chapterName)
                imgCollect.isChecked = it.collect
            }
        }
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int, item: Article?, payloads: List<Any>) {
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