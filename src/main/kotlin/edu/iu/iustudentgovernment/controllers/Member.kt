package edu.iu.iustudentgovernment.controllers

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post

fun Route.memberRoutes() {
    post("/edit-profile-submit/{username}") {

        call.respondRedirect("/me")


    }
}