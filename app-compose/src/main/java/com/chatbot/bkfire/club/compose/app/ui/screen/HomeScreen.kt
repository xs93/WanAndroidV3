package com.chatbot.bkfire.club.compose.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.chatbot.bkfire.club.compose.app.R
import com.chatbot.bkfire.club.compose.app.model.HomeTab
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/14 13:55
 * @email 466911254@qq.com
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            val (menuRef, tabRowRef, dividerRef) = remember {
                createRefs()
            }

            HorizontalDivider(
                modifier = Modifier.constrainAs(dividerRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )

            var tabIndex by remember { mutableIntStateOf(0) }
            PrimaryTabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                modifier = Modifier.constrainAs(tabRowRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.value(190.dp)
                },
                divider = {
                    HorizontalDivider(color = Color.Transparent)
                }
            ) {
                HomeTab.entries.forEachIndexed { index, homeTab ->
                    Tab(selected = index == tabIndex,
                        onClick = {
                            tabIndex = index
                        },
                        text = {
                            Text(
                                text = stringResource(id = homeTab.tabNameStringResId),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            val menuInteractionSource = remember {
                MutableInteractionSource()
            }
            Image(
                painter = painterResource(id = R.drawable.main_ic_menu_24dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .constrainAs(menuRef) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable(
                        interactionSource = menuInteractionSource,
                        indication = rememberRipple(bounded = false),
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ),
                contentDescription = null
            )


        }
    }
}


@Preview(showBackground = false, showSystemUi = false)
@Composable
fun HomeScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HomeScreen(drawerState)
}