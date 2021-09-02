package com.example.notefun2.domain.model.enum_class

import com.example.notefun2.utils.toLowerCaseExceptFirstChar

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