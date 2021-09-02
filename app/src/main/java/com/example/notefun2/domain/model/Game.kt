package com.example.notefun2.domain.model

import com.example.notefun2.domain.model.enum_class.AgeRating
import com.example.notefun2.domain.model.enum_class.GameMode
import com.example.notefun2.domain.model.enum_class.GenreGame
import com.example.notefun2.domain.model.enum_class.PlayerPerspective
import java.util.*

/**
 * Class reprensenting a Video Games
 */
data class Game(
    val id : Long,
    val name: String,
    val coverUrl: String,
    val rating: Double,
    val ratingCount: Int,
    val platformList: List<Platform>,
    val artWorkUrl: List<String> = emptyList(),
    val genres: List<GenreGame> = emptyList(),
    val firstReleaseDate: Date? = null,
    val release: List<Release> = emptyList(),
    val summary: String = "",
    val description: String = "",
    val screenShotUrl: List<String> = emptyList(),
    val videoList: List<Video> = emptyList(),
    val similiarGameId: List<Long> = emptyList(),
    val ageRatings: List<AgeRating> = emptyList(),
    val gameMode: List<GameMode> = emptyList(),
    val playerPerspective: List<PlayerPerspective> = emptyList(),
    val developer: List<GameCompany> = emptyList(),
    val publisher: List<GameCompany> = emptyList(),
    val websiteList: List<Website> = emptyList()
    )