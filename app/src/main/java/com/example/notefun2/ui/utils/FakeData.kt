package com.example.notefun2.ui.utils

import com.example.gamecore.domain.model.Game
import com.example.gamecore.domain.model.Platform
import com.example.gamecore.domain.model.Release
import com.example.gamecore.domain.model.enum_class.PlatformType
import java.util.*

val FakeGame = Game(
    id = 0,
    name = "Apex Legend",
    coverUrl = "https://upload.wikimedia.org/wikipedia/en/d/db/Apex_legends_cover.jpg",
    rating = 78.0,
    ratingCount = 289,
    platformList = listOf(
        Platform(PlatformType.WINDOWS, "Windows 10"),
        Platform(PlatformType.XBOX, "Xbox Serie X"),
        Platform(PlatformType.PLAYSTATION, "Playstation 5"),
        Platform(PlatformType.ANDROID, "Android"),
    ),
    artWorkUrl = listOf("https://media.contentapi.ea.com/content/dam/news/www-ea/images/2019/04/apex-featured-image-generic-lineup.jpg.adapt.crop191x100.628p.jpg")
)

val FakeGameList = listOf(
    FakeGame,
    FakeGame.copy(id = 1, rating = 69.0),
    FakeGame.copy(id = 2, rating = 69.0),
    FakeGame.copy(id = 3, rating = 45.0),
    FakeGame.copy(id = 5, rating = 98.0),
    FakeGame.copy(id = 6, rating = 33.0),
    FakeGame.copy(id = 7, rating = 88.0),
    FakeGame.copy(id = 8, rating = 67.0),
    FakeGame.copy(id = 9, rating = 23.0),
    FakeGame.copy(id = 10),
    FakeGame.copy(id = 11),
    FakeGame.copy(id = 12, rating = 44.0),
    FakeGame.copy(id = 13, rating = 72.0),
    FakeGame.copy(id = 14, rating = 30.0),
    FakeGame.copy(id = 15, rating = 79.0),
    FakeGame.copy(id = 16),
)

fun buildFakeGameList(count: Int): List<Game> {
    return List(count) {
        FakeGame.copy(
            name = "nameplaceholder",
            artWorkUrl = emptyList(),
            coverUrl = ""
        )
    }
}

fun buildFakeRelease(count: Int): List<Release> {
    return List(count) {
        Release(
            Date(),
            Platform(PlatformType.ANDROID, "Android"),
            "",
            0,
            "",
            "",
            "placeholder",
        )
    }
}