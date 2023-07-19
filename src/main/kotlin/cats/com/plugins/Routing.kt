package cats.com.plugins

import cats.com.controllers.meRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.util.*

fun Application.configureRouting() {
    
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        get("/") {
            call.respondText("Hello World!")
        }
        meRoutes()
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
