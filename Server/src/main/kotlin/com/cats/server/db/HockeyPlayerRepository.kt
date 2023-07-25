package com.cats.server.db

import com.cats.HockeyPlayer
import com.cats.HockeyPlayerQueries

interface HockeyPlayerRepository {

    suspend fun insertPlayer(player: HockeyPlayer)
    suspend fun getPlayers(): List<HockeyPlayer>
    suspend fun getPlayer(playerId: Int): HockeyPlayer

    suspend fun deletePlayer(playerId: Int)
}

class HockeyPlayerRepositoryImpl(private val queries: HockeyPlayerQueries) : HockeyPlayerRepository {
    override suspend fun insertPlayer(player: HockeyPlayer) {
        queries.upsert(player)
    }

    override suspend fun getPlayers(): List<HockeyPlayer> {
        return queries.selectAll(10).executeAsList()
    }

    override suspend fun getPlayer(playerId: Int): HockeyPlayer {
        return queries.selectByUid(playerId).executeAsOne()
    }

    override suspend fun deletePlayer(playerId: Int) {
        queries.remove(playerId)
    }

}