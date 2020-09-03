package edu.iu.iustudentgovernment.authentication

import edu.iu.iustudentgovernment.models.Idable


data class Member(
    val username: String,
    val constituency: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val bio: String? = null,
    var active: Boolean = true
) : Idable {

    override fun getPermanentId() = username
}


class User(val userId: String)