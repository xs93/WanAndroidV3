package com.chatbot.bkfire.club.compose.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.chatbot.bkfire.club.compose.app.R
import com.chatbot.bkfire.club.compose.app.model.MainTab
import com.chatbot.bkfire.club.compose.app.ui.theme.AppTheme
import com.chatbot.bkfire.club.compose.app.ui.theme.mainTabColorNormal
import com.chatbot.bkfire.club.compose.app.ui.widget.BottomNavigationBar
import com.chatbot.bkfire.club.compose.app.ui.widget.BottomNavigationBarItem
import com.chatbot.bkfire.club.compose.app.ui.widget.BottomNavigationBarItemColors
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/12/18 14:50
 * @email 466911254@qq.com
 */


@Composable
fun MainScreen() {
    AppTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { MainDrawerContent() }) {
            MainContent(drawerState)
        }
    }
}

@Composable
fun MainDrawerContent() {
    LazyColumn(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        item {
            ConstraintLayout {
                val (headerImageRef, photoImageRef, loginBtnRef) = remember { createRefs() }
                Image(
                    painter = painterResource(id = R.drawable.bg_main_drawer_header),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.constrainAs(headerImageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.matchParent
                        height = Dimension.preferredWrapContent
                    },
                    contentDescription = null
                )

                Image(
                    painter = painterResource(id = R.drawable.img_avatar_no_login),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(photoImageRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(headerImageRef.bottom)
                            width = Dimension.value(100.dp)
                            height = Dimension.value(100.dp)
                            verticalBias = 0.25f
                        }
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )

                Button(
                    onClick = {

                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .constrainAs(loginBtnRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(photoImageRef.bottom, margin = 24.dp)
                            width = Dimension.value(100.dp)
                            height = Dimension.value(32.dp)
                        },
                ) {
                    Text(
                        text = stringResource(id = R.string.main_drawer_click_login),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, 0f) {
        MainTab.entries.size
    }
    Scaffold(
        modifier = Modifier.windowInsetsPadding(NavigationBarDefaults.windowInsets),
        bottomBar = {
            val tabs = MainTab.entries.toTypedArray()
            BottomNavigationBar(modifier = Modifier.height(56.dp)) {
                tabs.forEachIndexed { index, mainTab ->
                    BottomNavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        icon = {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = mainTab.tabIconResId),
                                contentDescription = stringResource(id = mainTab.tabNameStringResId),
                                colorFilter = ColorFilter.tint(LocalContentColor.current),
                            )
                        },
                        label = {
                            if (pagerState.currentPage == index) {
                                Text(
                                    text = stringResource(id = mainTab.tabNameStringResId),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = stringResource(id = mainTab.tabNameStringResId),
                                    fontSize = 12.sp
                                )
                            }
                        },
                        iconLabelSpace = 4.dp,
                        colors = BottomNavigationBarItemColors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = mainTabColorNormal,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = mainTabColorNormal,
                            disabledIconColor = mainTabColorNormal.copy(alpha = 0.38f),
                            disabledTextColor = mainTabColorNormal.copy(alpha = 0.38f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondBoundsPageCount = MainTab.entries.size - 1
            ) { page ->
                when (page) {
                    0 -> HomeScreen(drawerState = drawerState)
                    1 -> ClassifyScreen()
                    2 -> SystemScreen()
                    3 -> MineScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}