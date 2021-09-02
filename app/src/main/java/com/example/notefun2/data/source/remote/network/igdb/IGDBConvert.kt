package com.example.notefun2.data.source.remote.network

import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.imageBuilder
import com.example.notefun2.domain.model.*
import com.example.notefun2.domain.model.enum_class.*
import com.example.notefun2.utils.toLowerCaseExceptFirstChar
import com.neovisionaries.i18n.CountryCode
import proto.AgeRatingRatingEnum
import proto.GameMode as GameModeConvert
import proto.GameVideo
import proto.WebsiteCategoryEnum
import java.lang.IllegalArgumentException
import proto.PlayerPerspective as PlayerPerspectiveConvert
import proto.ReleaseDate as ReleaseDateConvert
import java.util.*
import proto.Genre as GenreConvert
import proto.Game as GameConvert
import proto.Platform as PlatformConvert
import proto.InvolvedCompany as GameCompanyConvert
import proto.Website as WebsiteConvert

/**
 * Convert Data Transfer Object From IGDB to the Domain [Game] model
 */
fun GameConvert.toDomainGame(
    largeImageSize: ImageSize = ImageSize.SCREENSHOT_MEDIUM,
    coverSize: ImageSize = ImageSize.COVER_BIG
)= Game(
    id = id,
    name = name,
    genres = genresList.map(GenreConvert::toDomainGenre),
    platformList = platformsList.map(PlatformConvert::toDomainPlatform).toList(),
    firstReleaseDate = Date(firstReleaseDate.seconds * 1000),
    release = releaseDatesList.map(ReleaseDateConvert::toDomainRelease),
    summary = summary,
    description = storyline,
    coverUrl = imageBuilder(cover.imageId, coverSize),
    artWorkUrl = artworksList.map { imageBuilder(it.imageId, largeImageSize) },
    screenShotUrl = screenshotsList.map { imageBuilder(it.imageId, largeImageSize) },
    videoList = videosList.map(GameVideo::toDomainVideo),
    similiarGameId = similarGamesList.map { it.id },
    rating = totalRating,
    ratingCount = totalRatingCount,
    ageRatings = ageRatingsList.map {
        if (it.rating == AgeRatingRatingEnum.AGERATING_RATING_NULL){
            AgeRating.UNRECOGNIZED
        }else{
            AgeRating.valueOf(it.rating.name)
        }
    },
    gameMode = gameModesList.map(GameModeConvert::toDomainGameMode),
    playerPerspective = playerPerspectivesList.map(PlayerPerspectiveConvert::toDomainPPC),
    developer = involvedCompaniesList.filter { it.developer }
        .map(GameCompanyConvert::toDomainGCC),
    publisher = involvedCompaniesList.filter { it.publisher }
        .map(GameCompanyConvert::toDomainGCC),
    websiteList = websitesList
        .filter {
            it.category != WebsiteCategoryEnum.WEBSITE_CATEGORY_NULL && it.category != WebsiteCategoryEnum.UNRECOGNIZED
        }
        .map(WebsiteConvert::toDomainWebsite)
)

/**
 * Function for Convert "toDomain"
        */
//Genre
fun GenreConvert.toDomainGenre(): GenreGame {
    return try {
        GenreGame.valueOf(name.uppercase())
    }catch (e: IllegalArgumentException){
        GenreGame.OTHER
    }
}
//Platform
fun PlatformConvert.toDomainPlatform(): Platform{
    val platformName = name.lowercase()
    val platformType = when{
        platformName.contains("playstation") -> PlatformType.PLAYSTATION
        platformName.contains("xbox") -> PlatformType.XBOX
        platformName.contains("microsoft windows") -> PlatformType.WINDOWS
        platformName.contains("wii") -> PlatformType.WII
        platformName == "nintendo swicth" -> PlatformType.NINTENDO_SWITCH
        platformName == "android" -> PlatformType.ANDROID
        platformName == "mac" || platformName == "ios" -> PlatformType.APPLE
        platformName == "linux" -> PlatformType.LINUX
        else -> PlatformType.OTHER
    }
    return Platform(
        platformType = platformType,
        name = if (platformName.contains("microsoft windows")) "PC Windows" else name
    )
}

//Game Mode

fun GameModeConvert.toDomainGameMode(): GameMode{
    return when(name){
        "Single player" -> GameMode.SINGLE_PLAYER
        "Mutliplayer" -> GameMode.MULTIPLAYER
        "Co-operative" -> GameMode.COOPERATIVE
        "Split screen" -> GameMode.SPLIT_SCREEN
        "Massively Multiplayer Online (MMO)" -> GameMode.MMO
        "Battle Royale" -> GameMode.BATTLE_ROYALE
        else -> GameMode.OTHER
    }
}

//Player Perspective

fun PlayerPerspectiveConvert.toDomainPPC(): PlayerPerspective{
    return when(name){
        "First person" -> PlayerPerspective.FIRST_PERSON
        "Third person" -> PlayerPerspective.THIRD_PERSON
        "Auditory" -> PlayerPerspective.AUDITORY
        "Bird view" -> PlayerPerspective.BIRD_VIEW
        "Side view" -> PlayerPerspective.SIDE_VIEW
        "Text" -> PlayerPerspective.TEXT
        "Virtual Reality"-> PlayerPerspective.VIRTUAL_REALITY
        else -> PlayerPerspective.OTHER
    }
}

//Game company
fun GameCompanyConvert.toDomainGCC(): GameCompany{
    return GameCompany(
        id = company.id,
        name = company.name,
        description = company.description,
        country = CountryCode.getByCode(
            company.country
        )?.getName() ?: "",
        logoUrl = if (company.hasLogo()) imageBuilder(company.logo.imageId, ImageSize.LOGO_MEDIUM)
                    else ""
    )
}

//Video
fun GameVideo.toDomainVideo() = Video(
    url = "https://www.youtube.com/watch?v=${videoId}",
    title = name,
    thumnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
)

//Website

fun WebsiteConvert.toDomainWebsite() = when(category!!){
    WebsiteCategoryEnum.WEBSITE_OFFICIAL -> Website(
        url,
        "Official Website",
        "file:///android_asset/official_website_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_WIKIA -> Website(
        url,
        "Fandom (Wikia)",
        "file:///android_asset/wikia_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_WIKIPEDIA -> Website(
        url,
        "Wikipedia",
        "file:///android_asset/wikipedia_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_FACEBOOK -> Website(
        url,
        "Facebook",
        "file:///android_asset/facebook_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_TWITTER -> Website(
        url,
        "Twitter",
        "file:///android_asset/twitter_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_TWITCH -> Website(
        url,
        "Twitch",
        "file:///android_asset/twitch_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_INSTAGRAM -> Website(
        url,
        "Instagram",
        "file:///android_asset/instagram_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_YOUTUBE -> Website(
        url,
        "Youtube",
        "file:///android_asset/youtube_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_IPHONE -> Website(
        url,
        "App Store (Iphone)",
        "file:///android_asset/appstore_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_IPAD -> Website(
        url,
        "App store (Ipad)",
        "file:///android_asset/appstore_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_ANDROID -> Website(
        url,
        "Google Play Store",
        "file:///android_asset/play_store_icon.png",
    )
    WebsiteCategoryEnum.WEBSITE_STEAM -> Website(
        url,
        "Steam",
        "file:///android_asset/steam_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_REDDIT -> Website(
        url,
        "Reddit",
        "file:///android_asset/reddit_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_ITCH -> Website(
        url,
        "Itch",
        "file:///android_asset/itch_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_EPICGAMES -> Website(
        url,
        "Epic Games",
        "file:///android_asset/epic_games_logo.png",
    )
    WebsiteCategoryEnum.WEBSITE_GOG -> Website(
        url,
        "Gog",
        "file:///android_asset/gog_logo.png"
    )
    WebsiteCategoryEnum.WEBSITE_DISCORD -> Website(
        url,
        "Discord",
        "file:///android_asset/discord_logo.png",
    )
    WebsiteCategoryEnum.UNRECOGNIZED -> throw IllegalStateException()
    WebsiteCategoryEnum.WEBSITE_CATEGORY_NULL -> throw IllegalStateException()
}

//release

fun ReleaseDateConvert.toDomainRelease() = Release(
    date = Date(date.seconds * 1000),
    platform = platform.toDomainPlatform(),
    region = region.name.toLowerCaseExceptFirstChar().replace('_',' '),
    gameId = game.id,
    gameName = game.name,
    gameCoverUrl = imageBuilder(game.cover.imageId, ImageSize.COVER_BIG),
    artWorkUrl = when {
        game.artworksList.isNotEmpty() -> imageBuilder(
            game.artworksList.first().imageId,
            ImageSize.SCREENSHOT_MEDIUM,
        )
        game.screenshotsList.isNotEmpty() -> imageBuilder(
            game.screenshotsList.first().imageId,
            ImageSize.SCREENSHOT_MEDIUM
        )
        else -> ""
    }
)


