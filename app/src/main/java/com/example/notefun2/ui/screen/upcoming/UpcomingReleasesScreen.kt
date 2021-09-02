package com.example.notefun2.ui.screen.upcoming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notefun2.ui.Routes
import com.example.notefun2.ui.screen.shared.*
import com.example.notefun2.ui.theme.MajorMono
import com.example.notefun2.ui.utils.ReleaseCardPlaceholder
import com.example.notefun2.ui.utils.gameTypeNames
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UpcomingReleaseScreen(
    viewModel: UpcomingReleasesViewModel,
    navController: NavController,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    CollapsingTabBarScaffold(
        topAppBar = {
            NoteFunTopAppBar(
                title = { Text("Upcoming Release", fontSize = 18.sp,
                fontFamily = MajorMono) },
                drawerState = drawerState,
                // TODO implement sorting feature
//                actions = {
//                    Icon(
//                        imageVector = Icons.Default.Sort,
//                        contentDescription = null,
//                        modifier = Modifier.padding(end = 8.dp),
//                    )
//                }
            )
        },
        drawerState = drawerState,
        drawerContent = {
            Navigation
        }) {
        UpcomingReleaseScreenBody(
            viewModel = viewModel,
            navController = navController,
        )
    }
}

@Composable
fun UpcomingReleaseScreenBody(
    viewModel: UpcomingReleasesViewModel,
    navController: NavController,
) {
    val upcomingReleases by viewModel.upcomingReleases.observeAsState(emptyList())
    val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 250

    Column {
        Tabs(titles = gameTypeNames, viewModel::onGameTypeSelected)


        SwipeRefresh(
            onRefresh = { viewModel.loadNextPage() },
            bottomRefreshIndicatorState = rememberSwipeRefreshState(isRefreshing = viewModel.isNextPageLoading),
        ) {
            LazyColumn(Modifier.padding(horizontal = 16.dp)) {

                item {
                    Spacer(Modifier.height(16.dp))
                }

                if (upcomingReleases.isNullOrEmpty()) {
                    items(items = List(10) { it }.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (release in rowItems) {
                                ReleaseCardPlaceholder(
                                    Modifier
                                        .size(250.dp, 140.dp)
                                        .weight(1F),
                                )
                            }
                            // If the last row do not contains enough items to fill the row without
                            // expanding them, we add placeholders to keep the same size for all items.
                            repeat(numberOfItemsByRow - rowItems.size) {
                                Box(Modifier.weight(1F)) {}
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                } else {
                    items(items = upcomingReleases.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (release in rowItems) {
                                ReleaseCard(
                                    release = release,
                                    onClick = {
                                        navController.navigate(Routes.detailGames(release.gameId))
                                    },
                                    modifier = Modifier
                                        .size(250.dp, 140.dp)
                                        .weight(1F),
                                )
                            }
                            // If the last row do not contains enough items to fill the row without
                            // expanding them, we add placeholders to keep the same size for all items.
                            repeat(numberOfItemsByRow - rowItems.size) {
                                Box(Modifier.weight(1F)) {}
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                }

                if (viewModel.isLastPageReached) {
                    item {
                        Card(elevation = 15.dp, backgroundColor = Color.Gray.copy(0.1F)) {
                            Text(
                                "Oops, there are no more games to load, you have reached the end of the page.",
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

            }
        }
    }
}