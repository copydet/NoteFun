package com.example.gamecore.domain.model.enum_class

import com.example.gamecore.utils.toLowerCaseExceptFirstChar

enum class GameMode {
    BATTLE_ROYALE,
    MULTIPLAYER,
    SINGLE_PLAYER,
    MMO,
    SPLIT_SCREEN,
    COOPERATIVE,
    OTHER
    ;

    /**
     * Capitalized
     */

    override fun toString(): String {
        if (this == MMO){
            return "Massively Mutliplayer Online"
        }
        return name.replace('_', ' ').toLowerCaseExceptFirstChar()
    }
}