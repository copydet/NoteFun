package com.example.notefun2.data.source.remote.network.igdb

import com.example.notefun2.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.notefun2.domain.model.enum_class.GameMode
import com.example.notefun2.domain.model.enum_class.GenreGame
import java.lang.IllegalArgumentException

/**
 *
 */

fun regularizeGameCount(count: Int): Int{
    return when {
        count > ListGamesProvider.MAX_GAMES_COUNT_BY_REQUEST ->
            ListGamesProvider.MAX_GAMES_COUNT_BY_REQUEST
        count < 1 -> throw IllegalArgumentException("Parameter [count] most be positive number")
        else -> count
    }
}

/**
 *
 */

fun getGameGenreQuery(genre:GenreGame?, prefix: String = ""): String{
    if (genre != null){
        if (genre == GenreGame.ACTION) return "& ${prefix}themes.name = \"Action\""
        /*
        else if (genre == GenreGame.SURVIVAL) return "& ${prefix}theme.name = \"Survival\""
        else if (genre == GenreGame.FANTASY) return "& ${prefix}theme.name = \"Fantasy\""
        else if (genre == GenreGame.HORROR) return "& ${prefix}theme.name = \"Horror\""
        else if (genre == GenreGame.MOBA) return  "& ${prefix}theme.name = \"MOBA\""
        */
        return if (genre != GenreGame.OTHER){
            "& ${prefix}genres.name = \"${genre.igdbCompatibleName()}\""
        } else {
            val allGenreName = GenreGame.genreValue.map(GenreGame::igdbCompatibleName)
            "& ${prefix}genres.name != (${allGenreName.joinToString()}})"
        }
    }
    return ""
}

/**
 *
 */

fun getGameModeQuery(gameMode: GameMode?, prefix: String = ""): String {
    if (gameMode != null) {
        val modeName = when (gameMode) {
            GameMode.BATTLE_ROYALE -> "Battle Royale"
            GameMode.MULTIPLAYER -> "Multiplayer"
            GameMode.SINGLE_PLAYER -> "Single player"
            GameMode.MMO -> "Massively Multiplayer Online (MMO)"
            GameMode.SPLIT_SCREEN -> "Split screen"
            GameMode.COOPERATIVE -> "Co-operative"
            GameMode.OTHER -> throw IllegalArgumentException("The value `GameMode.OTHERS` is not supported yet")
        }
        return "& ${prefix}game_modes.name = \"$modeName\""
    }
    return ""
}

/**
 *
 */

fun getIGDBPlatformID(): String {
    val playstationIds = "167, 48, 9, 8, 165, 46, 38, 131" // PLAYSTATION 5, PLAYSTATION 4,...
    val xboxIds = "169, 49, 12, 11" // Xbox Series, Xbox One, Xbox 360...
    val windowsId = "6"
    val nintendoSwitchId = "130"
    val android = "34"
    val appleIds = "14, 39" // MacOS, IOS
    val linuxId = "3"
    val wiiIds = "5, 41" // WII, WII U
    return "$playstationIds, $xboxIds, $windowsId, $nintendoSwitchId, $android, $appleIds, $linuxId, $wiiIds"
}