package edu.iu.iustudentgovernment.data

import com.rethinkdb.RethinkDB.r
import edu.iu.iustudentgovernment.authentication.Member
import edu.iu.iustudentgovernment.connection
import edu.iu.iustudentgovernment.gson
import edu.iu.iustudentgovernment.models.Committee
import edu.iu.iustudentgovernment.models.Idable
import edu.iu.iustudentgovernment.models.Meeting
import edu.iu.iustudentgovernment.utils.asPojo
import edu.iu.iustudentgovernment.utils.queryAsArrayList
import java.util.concurrent.ConcurrentHashMap

private val membersTable = "users"
private val committeesTable = "committees"
private val committeeMembershipsTable = "committee_memberships"
private val meetingsTable = "meetings"
private val meetingMinutesTable = "meeting_minutes"
private val legislationTable = "legislation"
private val attendanceTable = "attendance"
private val committeeFilesTable = "committee_files"
private val meetingFilesTable = "meeting_files"
private val votesTable = "votes"
private val statementsTable = "statements"
private val keysTable = "keys"
private val messagesTable = "messages"
private val whitcombTable = "whitcomb"
private val complaintsTable = "complaints"

private val tables = listOf(
    membersTable to "username",
    legislationTable to "id",
    committeeMembershipsTable to "id",
    committeesTable to "id",
    meetingMinutesTable to "fileId",
    meetingsTable to "meetingId",
    attendanceTable to "attendenceId",
    meetingFilesTable to "fileId",
    committeeFilesTable to "fileId",
    votesTable to "voteId",
    statementsTable to "id",
    keysTable to "id",
    messagesTable to "id",
    whitcombTable to "week",
    complaintsTable to "id"
).toMap()

val caches = ConcurrentHashMap(
    listOf<Pair<String, MutableList<Idable>>>(
        membersTable to mutableListOf(),
        legislationTable to mutableListOf(),
        committeeMembershipsTable to mutableListOf(),
        committeesTable to mutableListOf(),
        meetingMinutesTable to mutableListOf(),
        meetingsTable to mutableListOf(),
        attendanceTable to mutableListOf(),
        meetingFilesTable to mutableListOf(),
        committeeFilesTable to mutableListOf(),
        votesTable to mutableListOf(),
        statementsTable to mutableListOf(),
        messagesTable to mutableListOf(),
        whitcombTable to mutableListOf(),
        complaintsTable to mutableListOf()
    ).toMap().toMutableMap()
)

class Database(val cleanse: Boolean) {

    init {
        println("Starting db setup")

        if (cleanse) {
            if (r.dbList().run<List<String>>(connection).contains("iusg")) {
                r.dbDrop("iusg").run<Any>(connection)
            }
            println("Dropped db")
        }

        println("Inserted db. Inserting tables")
        if (!r.dbList().run<List<String>>(connection).contains("iusg")) {
            r.dbCreate("iusg").run<Any>(connection)
            tables.forEach { (table, key) ->
                if (!r.tableList().run<List<String>>(connection).contains(table)) {
                    if (key != "id") r.tableCreate(table).optArg("primary_key", key).run<Any>(connection)
                    else r.tableCreate(table).run<Any>(connection)
                }
            }

        }

    }

    // members
    fun getMember(username: String): Member? = get(membersTable, username)

    // committees
    fun getCommittee(committeeId: String): Committee? = get(committeesTable, committeeId)
    fun getCommittees() = getAll<Committee>(committeesTable).sortedBy { it.formalName }

    // meetings
    fun getMeetings() = getAll<Meeting>(meetingsTable)
    fun getFutureMeetings(): List<Meeting> =
        getMeetings().filter { it.time > System.currentTimeMillis() }.sortedBy { it.time }

    fun <T : Idable> insert(table: String, obj: T): Any? {
        if (table in caches.keys) caches[table]!!.add(obj)
        return r.table(table).insert(r.json(gson.toJson(obj))).run<Any>(connection)
    }

    inline fun <reified T : Idable> getAll(table: String): List<T> {
        return if (table in caches.keys && caches[table]!!.isNotEmpty()) {
            caches[table]!!.mapNotNull { it as? T }
        } else {
            val values = r.table(table).run<Any>(connection).queryAsArrayList(gson, T::class.java).filterNotNull()
            caches[table]!!.addAll(values)

            values
        }
    }

    inline fun <reified T : Idable> get(table: String, id: Any): T? {
        return if (table in caches.keys) {
            val cache = caches[table]!!
            val found = cache.find { it.getPermanentId() == id }
            if (found == null) {
                val retrieved = asPojo(gson, r.table(table).get(id).run(connection), T::class.java)
                (retrieved as? Idable)?.let { cache.add(it) }
            }

            cache.find { it.getPermanentId() == id } as? T
        } else asPojo(gson, r.table(table).get(id).run(connection), T::class.java)
    }
}