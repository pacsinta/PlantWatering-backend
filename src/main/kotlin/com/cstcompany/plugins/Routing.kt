package com.cstcompany.plugins

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Application.configureRouting() {
    val client = HttpClient()
    var motor1 = false

    suspend fun changeMotorStatus(status: Boolean){
        val url = "http://192.168.68.103/motor1?motor1=" + if (status) 1 else 0
        client.request(url) {
            method = HttpMethod.Post
        }
    }

    suspend fun runForSecond(seconds: Int){
        changeMotorStatus(true)
        delay(seconds * 1000L)
        changeMotorStatus(false)
    }

    routing {
        route("/motor1") {
            get("/flip") {
                motor1 = !motor1

                changeMotorStatus(motor1)

                call.respondText(motor1.toString())
            }

            get("/timer") {
                val params = call.receiveParameters()
                val seconds = params["seconds"]?.toInt()

                launch {
                    if (seconds != null) {
                        runForSecond(seconds)
                    }
                }

                call.respond(HttpStatusCode.OK)
            }

            get("/off"){
                changeMotorStatus(false)
                call.respond(false)
            }

            get("/on"){
                changeMotorStatus(true)
                call.respond(true)
            }
        }
    }
}

data class Schedule(
    val days: Array<Boolean?> = arrayOfNulls(7),
    val hour: Int,
    val minutes: Int,
    val wateringTime: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (!days.contentEquals(other.days)) return false

        return true
    }
    override fun hashCode(): Int {
        return days.contentHashCode()
    }
}