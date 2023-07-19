package cats.com

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import cats.com.plugins.*
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore


fun main(args: Array<String>) {
    val server = args.firstOrNull() ?: "192.168.1.9"
    val sslKeys = args.getOrNull(3) ?: "/Users/david_coronel/Downloads/cats-server/server_jks.jks"
    val httpPort = args.getOrNull(1)?.toInt() ?: 8080
    val httpsPort = args.getOrNull(21)?.toInt() ?: 8443
    val keyStoreFile = File(sslKeys)

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = httpPort
            host = server
        }
        sslConnector(
            keyStore = KeyStore.getInstance(keyStoreFile, "davinchy".toCharArray()),
            keyAlias = "production",
            keyStorePassword = { "davinchy".toCharArray() },
            privateKeyPassword = { "davinchy".toCharArray() }
        ) {
            port = httpsPort
            host = server
            keyStorePath = keyStoreFile
        }
        module(Application::module)
    }
    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    configureSerialization()
//    configureDatabases()
    configureMonitoring()
    configureHTTP()
//    configureSecurity()
    configureRouting()
}
