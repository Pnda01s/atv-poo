val ktor_version = "2.3.8"
val kotlin_version = "1.9.22"
val kmongo_version = "4.11.0"
val koin_version = "3.5.3"
val logback_version = "1.4.14"

// ── FORÇAR RESOLUÇÃO DE CONFLITO ──
configurations.all {
    resolutionStrategy {
        // Força o Gradle a usar a versão 1.9.22 de qualquer biblioteca kotlin-test
        force("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")

        // Substitui qualquer tentativa de puxar o junit4 (antigo) pelo junit5
        dependencySubstitution {
            substitute(module("org.jetbrains.kotlin:kotlin-test-junit"))
                .using(module("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version"))
        }
    }
}

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    application
}

group = "br.com.filacidada"
version = "3.0.0"

application {
    // Como não há package no seu Application.kt, use apenas o nome do arquivo + Kt
    mainClass.set("ApplicationKt")
}



repositories {
    mavenCentral()
}

dependencies {
    // ── Ktor Server ──
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    // ── Serialização ──
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // ── MongoDB (KMongo) ──
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-serialization:$kmongo_version")

    // ── Koin (DI) ──
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // ── JWT / BCrypt / Swagger ──
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")

    // ── Outros ──
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // ══════════════ TESTES ══════════════

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // Kotlin Test focado apenas em JUnit 5
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    // Koin Test - Removido o exclude manual pois o bloco 'configurations.all' já resolve globalmente
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")

    testImplementation("io.mockk:mockk:1.13.9")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:mongodb:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
}

tasks.test {
    useJUnitPlatform()
}
tasks.register<JavaExec>("seed") {
    description = "Popula o banco MongoDB com dados de exemplo"
    group = "application"
    mainClass.set("DatabaseSeedKt")
    classpath = sourceSets["main"].runtimeClasspath
}

kotlin {
    jvmToolchain(21)
}