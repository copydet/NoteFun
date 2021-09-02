package com.example.notefun2.ui.utils

import com.example.notefun2.R
import com.example.notefun2.domain.model.Game
import com.example.notefun2.domain.model.enum_class.GenreGame
import com.example.notefun2.domain.model.enum_class.PlatformType
import java.text.SimpleDateFormat
import java.util.*

val PlatformType.logo: Int
    get() = when(this){
        PlatformType.PLAYSTATION -> R.drawable.ic_playstation_logo
        PlatformType.XBOX -> R.drawable.ic_xbox_logo
        PlatformType.WINDOWS -> R.drawable.ic_windows_logo
        PlatformType.NINTENDO_SWITCH -> R.drawable.ic_nintendo_switch_logo
        PlatformType.ANDROID -> R.drawable.ic_android_logo
        PlatformType.APPLE -> R.drawable.ic_apple_logo
        PlatformType.LINUX -> R.drawable.ic_linux_logo
        PlatformType.WII -> R.drawable.ic_wii_logo
        PlatformType.OTHER -> R.drawable.ic_add
    }

val Game.bannerUrl: String
    get() = when {
        artWorkUrl.isNotEmpty() -> artWorkUrl.first()
        screenShotUrl.isNotEmpty() -> screenShotUrl.first()
        else -> "" // TODO create a placeholder
    }

val Game.allImageUrls: List<String>
    get() = artWorkUrl + screenShotUrl

val Game.formattedInitialReleaseDate: String
    get() {
        return SimpleDateFormat.getDateInstance().format(firstReleaseDate!!)
    }

val gameTypeNames: List<String>
    get() = listOf("ALL") + listOf(
        *GenreGame.genreName.toTypedArray(),
        "BATTLE ROYALE",
        "MULTIPLAYER",
        "SINGLE PLAYER",
    ).sorted()


fun getYearTimestamp(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Long {
    return Calendar.getInstance().apply { set(year, 0, 1) }.timeInMillis
}