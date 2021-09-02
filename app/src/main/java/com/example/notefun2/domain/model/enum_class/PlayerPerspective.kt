package com.example.notefun2.domain.model.enum_class

import com.example.notefun2.utils.toLowerCaseExceptFirstChar

enum class PlayerPerspective {
    FIRST_PERSON,
    THIRD_PERSON,
    BIRD_VIEW,
    SIDE_VIEW,
    VIRTUAL_REALITY,
    AUDITORY,
    TEXT,
    OTHER
    ;

    /**
     * return a capitalized form of the current Entry
     */

    override fun toString() = name.replace('_', ' ').toLowerCaseExceptFirstChar()
}