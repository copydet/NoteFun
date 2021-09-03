package com.example.notefun2.ui

import com.example.notefun2.R

sealed class ScreenNavigation(var route: String, var icon: Int, var title: String){
    object Home : ScreenNavigation("home", R.drawable.ic_game_logo, "Games")
    object Profile : ScreenNavigation("profile", R.drawable.upcoming_release_icon, "Profile Example")
}

object Routes {
    const val home = "home"
    const val detailGames = "game_details/{gameId}"
    const val newGames = "new_games/{minReleaseTimeStamp}/{subtitle}"
    const val upcomingRelease = "upcoming_releases"
    const val popularGames = "popular_games/{minReleaseTimeStamp}/{subtitle}"
    const val search = "search"

    fun detailGames(gameId: Long) = "game_details/$gameId"
    fun newGames(minReleaseTimeStamp: Long, subtitle: String) =
        "new_games/$minReleaseTimeStamp/$subtitle"

    fun popularGames(minReleaseTimeStamp: Long, subtitle: String) =
        "popular_games/$minReleaseTimeStamp/$subtitle"
}