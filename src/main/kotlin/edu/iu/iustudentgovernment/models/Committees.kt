package edu.iu.iustudentgovernment.models

import edu.iu.iustudentgovernment.database

data class Committee(
    val formalName: String,
    val id: String,
    var chairUsername: String,
    val permissionLevelForEntry: Int,
    var descriptionId: String = "committee_description_$id"
) : Idable {
    override fun getPermanentId() = id

    val upcomingMeetings get() = database.getFutureMeetings().filter { it.committeeId == id }.take(3)
}

data class Meeting(
    val name: String,
    val meetingId: String,
    val time: Long,
    val location: String,
    val committeeId: String,
    var agendaFileUrl: String?,
    val ledBy: List<String>,
    val minutesUrl: String? = null
) : Idable {

    val committee get() = database.getCommittee(committeeId)

    override fun getPermanentId() = meetingId

}

