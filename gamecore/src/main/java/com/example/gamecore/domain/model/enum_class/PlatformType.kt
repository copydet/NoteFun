package com.example.gamecore.domain.model.enum_class

import com.example.gamecore.utils.toLowerCaseExceptFirstChar

enum class PlatformType {
    PLAYSTATION,
    XBOX,
    WINDOWS,
    NINTENDO_SWITCH,
    ANDROID,
    APPLE,
    LINUX,
    WII,
    OTHER
    ;

    override fun toString(): String {
        return name.toLowerCaseExceptFirstChar()
    }
}