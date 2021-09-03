package com.example.gamecore.domain.model

import com.example.gamecore.domain.model.enum_class.PlatformType

data class Platform(
    val platformType: PlatformType,
    val name: String,
)