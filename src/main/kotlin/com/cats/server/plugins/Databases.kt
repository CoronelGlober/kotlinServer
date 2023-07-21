package com.cats.server.plugins

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.cats.HockeyPlayer
import com.cats.HockeyPlayerQueries
import com.cats.Transactions
import com.cats.server.db.HockeyPlayerRepository
import com.cats.server.io.models.HockeyPlayerDTO
import com.cats.server.io.models.fromDb
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database


fun Application.configureDatabases(playerRepository: HockeyPlayerRepository) {


    routing {
        // Create user
        post("/users") {
            val user = call.receive<HockeyPlayerDTO>()
            val id = playerRepository.insertPlayer(user.toDb())
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = playerRepository.getPlayer(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user.fromDb())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Read user
        get("/users") {
            val user = playerRepository.getPlayers()
            if (user != null) {
                call.respond(HttpStatusCode.OK, user.map { it.fromDb() } )
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
//        put("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val user = call.receive<ExposedUser>()
//            userService.update(id, user)
//            call.respond(HttpStatusCode.OK)
//        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            playerRepository.deletePlayer(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
