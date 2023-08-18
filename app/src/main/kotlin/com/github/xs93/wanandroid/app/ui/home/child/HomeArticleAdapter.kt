package com.github.xs93.wanandroid.app.ui.home.child

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseDifferAdapter
import com.github.xs93.wanandroid.app.databinding.HomeArticleItemBinding
import com.github.xs93.wanandroid.common.diff.ArticleItemCallback
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
class HomeArticleAdapter :
    BaseDifferAdapter<Article, HomeArticleAdapter.HomeArticleViewHolder>(ArticleItemCallback()) {

    class HomeArticleViewHolder(val bind: HomeArticleItemBinding) :
        RecyclerView.ViewHolder(bind.root)


    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): HomeArticleViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = HomeArticleItemBinding.inflate(layoutInflater, parent, false)
        return HomeArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int, item: Article?) {
        holder.bind.article = item
    }
}