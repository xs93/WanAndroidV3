@file:OptIn(ExperimentalFoundationApi::class)

package com.github.xs93.wanandroid.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.ui.theme.AppTheme
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreViewModel
import com.github.xs93.wanandroid.app.ui.widget.Banner
import com.github.xs93.wanandroid.common.entity.Article
import com.orhanobut.logger.Logger

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 10:52
 * @email 466911254@qq.com
 */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel()) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Banner(
                dataList = uiState.banners,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(16 / 9f),
                autoLoop = true,
                onItemClick = {
                    ToastManager.showToast(it.title)
                },
                indicatorModifier = Modifier.padding(start = 16.dp, bottom = 8.dp, end = 16.dp),
                indicatorAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    model = it.imagePath,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

        items(uiState.articles) {
            Logger.d("22222222222")
            ArticleCardItem(article = it)
        }
    }
}

@Composable
fun ArticleCardItem(article: Article) {
    Card(
        onClick = { ToastManager.showToast(article.title) },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            val (topInfoRef, dateRef, titleRef, typeRef) = createRefs()

            Row(modifier = Modifier.constrainAs(topInfoRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }) {
                val articleName = article.author.ifBlank { article.shareUser }
                Text(
                    text = articleName,
                    fontSize = 13.sp
                )
                val hasTopFlag = article.type == 1
                if (hasTopFlag) {
                    Text(
                        text = stringResource(id = R.string.article_tag_top),
                        color = colorResource(id = R.color.article_tag_red),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                val hasFreshFlag = article.fresh
                if (hasFreshFlag) {
                    Text(
                        text = stringResource(id = R.string.article_tag_new),
                        color = colorResource(id = R.color.article_tag_red),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                val tags = article.tags
                tags.forEach {
                    Text(
                        text = it.name,
                        color = colorResource(id = R.color.article_tag_green),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
            Text(
                text = article.niceDate,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(dateRef) {
                    end.linkTo(parent.end)
                    top.linkTo(topInfoRef.top)
                    bottom.linkTo(topInfoRef.bottom)
                }
            )

            Text(text = article.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(topInfoRef.bottom, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    horizontalBias = 0f
                }
            )
            val typeContext = "${article.superChapterName}·${article.chapterName}"
            Text(
                text = typeContext,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(typeRef) {
                    start.linkTo(topInfoRef.start)
                    top.linkTo(titleRef.bottom, 12.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewExploreScreen() {
    AppTheme {
        ExploreScreen()
    }
}