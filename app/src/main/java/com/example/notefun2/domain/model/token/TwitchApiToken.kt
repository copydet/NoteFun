package com.example.notefun2.domain.model.token

data class TwitchApiToken(
    val access_token: String,
    val expires_in: Long,
    val token_type: String
)
