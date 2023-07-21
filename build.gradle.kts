val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("app.cash.sqldelight") version "2.0.0-rc02"
}

group = "com.cats.server"
version = "0.0.1"
application {
    mainClass.set("com.cats.server.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-http-redirect-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("app.cash.sqldelight:jdbc-driver:2.0.0-rc02")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

sqldelight {
    databases {
        create("Transactions") {
            //srcDirs("sqldelight")
            //deriveSchemaFromMigrations.set(true)
            packageName.set("com.cats")
            dialect("app.cash.sqldelight:mysql-dialect:2.0.0-rc02")
            verifyMigrations.set(true)
            //schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            migrationOutputDirectory.set(file("src/main/sqldelight/migrations"))
        }
    }
}