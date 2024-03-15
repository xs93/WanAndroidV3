@file:OptIn(ExperimentalFoundationApi::class)

package com.github.xs93.wanandroid.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.utils.ktx.toHtml
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.router.AppNavHost
import com.github.xs93.wanandroid.app.router.RouteConfig
import com.github.xs93.wanandroid.app.ui.theme.AppTheme
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreViewModel
import com.github.xs93.wanandroid.app.ui.widget.Banner
import com.github.xs93.wanandroid.common.entity.Article

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
            ElevatedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 4.dp),
                modifier = Modifier.padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp)
            ) {
                Banner(
                    dataList = uiState.banners,
                    modifier = Modifier.aspectRatio(16 / 9f),
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
        }
        items(uiState.articles) {
            ArticleCardItem(article = it)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ArticleCardItem(article: Article) {
    ElevatedCard(
        onClick = {
            AppNavHost.navController.navigate(RouteConfig.ROUTE_ARTICLE_DETAIL)
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 4.dp),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topInfoRef, dateRef, titleRef, typeRef, collectRef) = createRefs()

            Row(
                modifier = Modifier.constrainAs(topInfoRef) {
                    start.linkTo(parent.start, 12.dp)
                    top.linkTo(parent.top, 12.dp)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                val articleName = article.author.ifBlank { article.shareUser }
                Text(
                    text = articleName,
                    fontSize = 12.sp,
                )
                val hasTopFlag = article.type == 1
                if (hasTopFlag) {
                    Text(
                        text = stringResource(id = R.string.article_tag_top),
                        modifier = Modifier.padding(start = 8.dp),
                        color = colorResource(id = R.color.article_tag_red),
                        fontSize = 10.sp,
                    )
                }

                val hasFreshFlag = article.fresh
                if (hasFreshFlag) {
                    Text(
                        text = stringResource(id = R.string.article_tag_new),
                        color = colorResource(id = R.color.article_tag_red),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                val tags = article.tags
                tags.forEach {
                    Text(
                        text = it.name,
                        color = colorResource(id = R.color.article_tag_green),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Text(text = article.niceDate,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(dateRef) {
                    top.linkTo(topInfoRef.top)
                    bottom.linkTo(topInfoRef.bottom)
                    end.linkTo(parent.end, 12.dp)
                })

            Text(
                text = article.title.toHtml().toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(topInfoRef.bottom, 8.dp)
                    start.linkTo(parent.start, 12.dp)
                    end.linkTo(parent.end, 12.dp)
                    horizontalBias = 0f
                    width = Dimension.fillToConstraints
                }

            )
            val typeContext = "${article.superChapterName}·${article.chapterName}"
            Text(text = typeContext,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(typeRef) {
                    start.linkTo(topInfoRef.start)
                    top.linkTo(titleRef.bottom, 6.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                })

            val collectResId = if (article.collect) {
                com.github.xs93.common.R.drawable.common_ic_collect
            } else {
                com.github.xs93.common.R.drawable.common_ic_un_collect
            }

            Image(painter = painterResource(id = collectResId),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(collectRef) {
                        end.linkTo(parent.end, 12.dp)
                        top.linkTo(typeRef.top)
                        bottom.linkTo(typeRef.bottom)
                    })
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