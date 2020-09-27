package edu.iu.iustudentgovernment.controllers

import edu.iu.iustudentgovernment.http.HandlebarsContent
import edu.iu.iustudentgovernment.http.respondHbs
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.searchRoutes() {
    get("/syllabus-search") {

        call.respondHbs(HandlebarsContent("search-result.hbs", null))
    }
}