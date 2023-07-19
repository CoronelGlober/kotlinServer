package cats.com.controllers

import io.ktor.server.application.*
import io.ktor.server.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Routing.meRoutes() {

    get("/me") {
        val name = call.request.queryParameters["name"] ?: "dear unknown"
        call.respondText("Hello $name")
    }

    get("/now") {
        call.respondText("It is ${LocalDateTime.now().toHttpDateString()}")
    }
}