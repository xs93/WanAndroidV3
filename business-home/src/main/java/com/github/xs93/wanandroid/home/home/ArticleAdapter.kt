package com.github.xs93.wanandroid.home.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.github.xs93.core.ktx.dp
import com.github.xs93.wanandroid.common.model.Article
import com.github.xs93.wanandroid.home.R
import com.github.xs93.wanandroid.home.databinding.HomeItemArticleBinding

/**
 * 文字列表适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/2 10:51
 * @email 466911254@qq.com
 */
class ArticleAdapter :
    BaseQuickAdapter<Article, BaseDataBindingHolder<HomeItemArticleBinding>>(R.layout.home_item_article) {

    init {
        addChildClickViewIds(R.id.civ_favorite)
    }

    override fun convert(holder: BaseDataBindingHolder<HomeItemArticleBinding>, item: Article) {
        val dataBinding = holder.dataBinding
        dataBinding?.let {
            it.article = item
            if (item.tags.isNotEmpty()) {
                it.llArticleOtherTag.visibility = View.VISIBLE
                val tagChildSize = it.llArticleOtherTag.childCount
                if (tagChildSize < item.tags.size) {
                    val addCount = item.tags.size - tagChildSize
                    for (index in 0 until addCount) {
                        addTagView(context, it.llArticleOtherTag)
                    }
                } else {
                    for (index in item.tags.size until tagChildSize) {
                        val childView = it.llArticleOtherTag.getChildAt(index)
                        childView.visibility = View.GONE
                    }
                }
                for (index in item.tags.indices) {
                    val childView = it.llArticleOtherTag.getChildAt(index)
                    if (childView is TextView) {
                        childView.visibility = View.VISIBLE
                        childView.text = item.tags[index].name
                    }
                }
            } else {
                it.llArticleOtherTag.visibility = View.GONE
            }

            var authorInfo = ""
            if (item.author.isNotBlank()) {
                authorInfo = context.getString(com.github.xs93.wanandroid.common.R.string.article_author, item.author)
            } else if (item.shareUser.isNotEmpty()) {
                authorInfo =
                    context.getString(com.github.xs93.wanandroid.common.R.string.article_share_user, item.shareUser)
            }
            it.tvAuthor.text = authorInfo


            val chapterName = when {
                item.superChapterName.isNotBlank() and item.chapterName.isNotBlank() -> "${item.superChapterName} / ${item.chapterName}"
                item.superChapterName.isNotBlank() -> item.superChapterName
                item.chapterName.isNotBlank() -> item.chapterName
                else -> ""
            }
            it.tvArticleChapterName.text = chapterName
            it.tvTitle.text = item.title.parseAsHtml()
        }
    }


    private fun addTagView(context: Context, parent: LinearLayout) {
        val inflater = LayoutInflater.from(context)
        val rootView =
            inflater.inflate(com.github.xs93.wanandroid.common.R.layout.common_article_list_tag, parent, false)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(-2, -2)
        layoutParams.marginStart = 4.dp(context)
        parent.addView(rootView, layoutParams)
    }
}