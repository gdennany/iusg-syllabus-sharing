package edu.iu.iustudentgovernment.controllers

import edu.iu.iustudentgovernment.data.getMap
import edu.iu.iustudentgovernment.http.HandlebarsContent
import edu.iu.iustudentgovernment.http.respondHbs
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.homeRoutes() {
    get("/") {
        val map = getMap(call, "Home")

        call.respondHbs(HandlebarsContent("syllabus-landing-page.hbs", map))
    }
}