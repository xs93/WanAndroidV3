package com.github.xs93.wanandroid.home.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
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
    override fun convert(holder: BaseDataBindingHolder<HomeItemArticleBinding>, item: Article) {
        holder.dataBinding?.article = item
    }
}