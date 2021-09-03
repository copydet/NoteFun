package com.example.gamecore.domain.model.enum_class

import com.example.gamecore.utils.toLowerCaseExceptFirstChar

enum class GenreGame {
    ADVENTURE,
    ACTION,
    ARCADE,
    RACING,
    RPG,
    SHOOTER,
    SIMULATOR,
    STRATEGY,
//    HORROR, cant invoke this, i will try to check endpoint on postman
//    SURVIVAL, cant invoke this
//    FANTASY, cant invoke this
    PUZZLE,
//    MOBA, // cant invoke this
    MUSIC,
    OTHER
    ;

    override fun toString() = name.replace('_',' ').toLowerCaseExceptFirstChar()

    companion object {
        /**
         * All Values Kecuali [OTHER]
         */
        val genreValue = values().filter { it != OTHER }

        /**
         * All Values Except for [OTHER]
         */
        val genreName = genreValue.map { it.name }
    }
}