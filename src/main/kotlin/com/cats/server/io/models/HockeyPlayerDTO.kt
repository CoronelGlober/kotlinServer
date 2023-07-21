package com.cats.server.io.models

import com.cats.HockeyPlayer
import kotlinx.serialization.Serializable

@Serializable
public data class HockeyPlayerDTO(val playerNumber: Int, val fullName: String) {
    fun toDb() = HockeyPlayer(playerNumber, fullName)
}

fun HockeyPlayer.fromDb() = HockeyPlayerDTO(player_number, full_name)