package com.cstcompany

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.cstcompany.plugins.*

fun main() {
    embeddedServer(Netty, port = 8001, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
