package com.example.notefun2.domain.model

import com.example.notefun2.domain.model.enum_class.PlatformType

data class Platform(
    val platformType: PlatformType,
    val name: String,
)