package com.example.notefun2.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.notefun2.ui.screen.detail.GameDetailsScreen
import com.example.notefun2.ui.screen.detail.GameDetailsViewModel
import com.example.notefun2.ui.screen.example.ProfileScreenExample
import com.example.notefun2.ui.screen.home.HomeScreen
import com.example.notefun2.ui.screen.newgames.NewGamesScreen
import com.example.notefun2.ui.screen.newgames.NewGamesViewModel
import com.example.notefun2.ui.screen.popular.PopularGamesScreen
import com.example.notefun2.ui.screen.popular.PopularGamesViewModel
import com.example.notefun2.ui.screen.search.SearchScreen
import com.example.notefun2.ui.screen.upcoming.UpcomingReleaseScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Preview
@Composable
fun Navigation(){
    val items = listOf(
        ScreenNavigation.Home,
        ScreenNavigation.Profile
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(bottomBar = {
        if (currentDestination == Routes.home) {
        BottomNavigation {
            items.forEach { screen ->
                val selected =
                    navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true

                BottomNavigationItem(
                    icon = {
                        val iconSize = if (selected) 40.dp else 20.dp
                        Icon(
                            painterResource(id = screen.icon), contentDescription = screen.title,
                            modifier = Modifier
                                .padding(horizontal = 7.dp)
                                .size(iconSize)
                        )
                    },
                    modifier = Modifier.padding(bottom = 0.dp),
                    selectedContentColor = Color.Unspecified,
                    unselectedContentColor = Color.White,
                    //    label = { Text(text = screen.title) },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                )
            }

        }
    }
    }) {
        Box(modifier = Modifier.padding(it)) {

        NavHost(
            navController = navController,
            startDestination = ScreenNavigation.Home.route,
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {

            composable(ScreenNavigation.Home.route) {
                HomeScreen(viewModel = hiltViewModel(), navController = navController)
            }

            composable(ScreenNavigation.Profile.route) {
                ProfileScreenExample()
            }

            composable(Routes.search) {
                SearchScreen(viewModel = hiltViewModel(), navController = navController)
            }

            composable(Routes.upcomingRelease) {
                UpcomingReleaseScreen(viewModel = hiltViewModel(), navController = navController)
            }

            composable(Routes.newGames, arguments = listOf(navArgument("minReleaseTimeStamp") {
                type = NavType.LongType
            })) { backStackEntry ->
                val minReleaseTimestamp = backStackEntry.arguments!!.getLong("minReleaseTimeStamp")
                val subtitle = backStackEntry.arguments!!.getString("subtitle")!!
                NewGamesScreen(
                    viewModel = hiltViewModel<NewGamesViewModel>().apply {
                        initialize(
                            minReleaseTimestamp
                        )
                    },
                    navController = navController,
                    subtitle = subtitle,
                )
            }

            composable(Routes.popularGames, arguments = listOf(navArgument("minReleaseTimeStamp") {
                type = NavType.LongType
            })) { backStackEntry ->
                val minReleaseTimestamp = backStackEntry.arguments!!.getLong("minReleaseTimeStamp")
                val subtitle = backStackEntry.arguments!!.getString("subtitle")!!
                val viewModel = hiltViewModel<PopularGamesViewModel>().apply {
                    initializes(minReleaseTimestamp)
                }

                PopularGamesScreen(
                    viewModel = viewModel,
                    navController = navController,
                    subtitle = subtitle,
                )
            }

            composable(
                Routes.detailGames,
                arguments = listOf(navArgument("gameId") { type = NavType.LongType })
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments!!.getLong("gameId")
                GameDetailsScreen(
                    viewModel = hiltViewModel<GameDetailsViewModel>().apply { initialize(gameId) },
                    navController = navController,
                )
            }
        }
    }

    }

}